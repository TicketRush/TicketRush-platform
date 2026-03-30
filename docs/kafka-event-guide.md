# Kafka Event Guide

## 1. Architecture & Configuration Overview (`KafkaConfig`)
본 프로젝트의 Kafka 설정은 마이크로서비스 환경에서 **메시지 유실 방지(Zero Data Loss)** 와 **장애 격리/복구(Resilience)** 에 초점을 맞추어 설계되었습니다.

### 1.1. Producer (발행자) 주요 정책
* **신뢰성 100% 보장:** `acks=all` 및 `enable.idempotence=true`를 적용하여 카프카 브로커에 메시지가 완벽히 복제되었는지 확인하며, 네트워크 재시도 시 발생할 수 있는 중복 발행을 원천 차단합니다.
* **재시도 및 타임아웃:** 전송 실패 시 최대 3회(`PRODUCER_RETRIES`) 재시도하며, 최대 120초(`DELIVERY_TIMEOUT_MS`)까지 전송을 보장하기 위해 대기합니다.
* **Envelope 패턴 적용:** 모든 이벤트는 `DomainEventEnvelope`라는 규격화된 객체로 포장되어 전송되며, 내부 페이로드(데이터)는 JSON 형태로 안전하게 직렬화됩니다.

### 1.2. Consumer (수신자) 및 에러 핸들링 정책
* **수동 커밋 (Manual Commit):** `AckMode.MANUAL_IMMEDIATE`를 설정하여, 컨슈머가 이벤트 처리를 완벽하게 끝마쳤을 때만 오프셋을 커밋합니다. (처리 도중 서버가 죽어도 데이터 유실 없음)
* **지수 백오프(Exponential Backoff) 재시도:** 일시적인 장애(예: DB 락, 일시적 네트워크 단절) 발생 시 즉시 실패 처리하지 않고, 점진적으로 대기 시간을 늘려가며 최대 5회 재시도합니다 (초기 1초 → 최대 60초 대기).
* **DLT (Dead Letter Topic) 자동 라우팅:** 최대 재시도 횟수를 초과하거나, 역직렬화 실패(`DeserializationException`) 같은 논리적 오류 발생 시, 해당 메시지를 유실시키지 않고 `[원본토픽명].DLT` 토픽으로 자동 격리합니다. 이를 통해 정상 메시지 처리가 지연되는 것을 막고 실패한 메시지만 추후 분석할 수 있습니다.

---

## 2. 사용 가이드 (How to Use)

새로운 비즈니스 로직에서 카프카로 이벤트를 발행하려면 다음 **2가지 단계**만 거치면 됩니다. 카프카 인프라 기술에 종속되지 않고 비즈니스 로직에만 집중할 수 있습니다.

### Step 1. 커스텀 이벤트 클래스 정의하기
`DomainEvent` 인터페이스를 구현하는 레코드(또는 클래스)를 생성합니다. 이곳에 어떤 토픽으로 보낼지, 키는 무엇인지 정의합니다.

예시 코드

```java
package com.ticketrush.domain.order.event;

import com.ticketrush.global.event.DomainEvent;
import com.ticketrush.global.event.EventUtils;

// 전송할 실제 데이터들 (Payload에 들어갈 내용)
public record OrderCompletedEvent(
    String orderId,
    Long userId,
    int totalAmount
) implements DomainEvent {

    @Override
    public String topic() {
        // 이 이벤트가 전송될 카프카 토픽명
        return "order-events"; 
    }

    @Override
    public String key() {
        // 파티션 분배를 위한 Key (여기서는 동일한 유저의 주문은 순서를 보장하기 위해 userId 사용)
        return String.valueOf(userId); 
    }

    @Override
    public String eventName() {
        // 이벤트 종류 명시
        return "OrderCompleted"; 
    }

    @Override
    public String traceId() {
        // 로깅 및 분산 추적을 위한 Trace ID 자동 추출
        return EventUtils.extractTraceId(); 
    }
}
```

### Step 2. 비즈니스 로직에서 `EventPublisher` 호출하기
`KafkaTemplate`을 직접 조작하지 마세요. `EventPublisher` 인터페이스를 주입받아 직관적으로 발행(`publish`)합니다.

```java
package com.ticketrush.domain.order.service;

import com.ticketrush.domain.order.event.OrderCompletedEvent;
import com.ticketrush.global.eventpublisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    // KafkaDomainEventPublisher가 자동으로 주입됩니다.
    private final EventPublisher eventPublisher;

    @Transactional
    public void completeOrder(String orderId, Long userId, int amount) {
        // 1. 비즈니스 로직 실행
        // ... 주문 완료 처리 로직 ...

        // 2. 이벤트 객체 생성
        OrderCompletedEvent event = new OrderCompletedEvent(orderId, userId, amount);

        // 3. 카프카로 이벤트 발행 (내부적으로 JSON 직렬화 및 Envelope 포장이 일어남)
        eventPublisher.publish(event);
    }
}
```

### (참고) 발행된 이벤트를 수신하는 방법 (Consumer)
발행된 데이터는 `DomainEventEnvelope` 형태로 수신됩니다. 수신 후 `payload` 필드를 역직렬화하여 사용합니다.

```java
import com.ticketrush.domain.order.event.OrderCompletedEvent;
import com.ticketrush.global.event.DomainEventEnvelope;
import com.ticketrush.global.json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

  // Publisher에서 직렬화할 때 썼던 동일한 JsonConverter를 주입받습니다.
  private final JsonConverter jsonConverter;

  @KafkaListener(topics = "order-events", groupId = "notification-group")
  public void handleOrderEvent(DomainEventEnvelope envelope) {

    // 1. 봉투(Envelope)의 메타데이터를 확인하여 처리할 이벤트인지 검증
    if ("OrderCompleted".equals(envelope.eventType())) {

      try {
        // 2. String 형태의 payload를 실제 객체(OrderCompletedEvent)로 역직렬화
        OrderCompletedEvent event = jsonConverter.deserialize(
          envelope.payload(),
          OrderCompletedEvent.class
        );

        // 3. 역직렬화된 객체의 필드값들을 활용하여 비즈니스 로직 처리
        // 유저에게 카카오톡 알림 발송, 통계 데이터 적재 등 필요한 로직 수행

      } catch (Exception e) {
        // 필요시 예외를 던져서 KafkaConfig에 설정해둔 DLT로 넘어가게 할 수 있습니다.
        log.error("이벤트 역직렬화 또는 처리 중 에러 발생: eventId={}", envelope.eventId(), e);
        throw e;
      }
    }
  }
}
```