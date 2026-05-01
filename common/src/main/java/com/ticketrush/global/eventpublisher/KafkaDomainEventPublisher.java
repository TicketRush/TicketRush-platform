package com.ticketrush.global.eventpublisher;

import com.ticketrush.global.event.DomainEvent;
import com.ticketrush.global.event.DomainEventEnvelope;
import com.ticketrush.global.exception.BusinessException; // 추가
import com.ticketrush.global.json.JsonConverter;
import com.ticketrush.global.status.ErrorStatus; // 추가
import java.util.concurrent.ExecutionException; // 추가
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnExpression("'${app.event-publisher.type}' == 'kafka'")
@RequiredArgsConstructor
public class KafkaDomainEventPublisher implements EventPublisher {

  private final KafkaTemplate<String, DomainEventEnvelope> kafkaTemplate;
  private final JsonConverter jsonConverter;

  @Override
  public void publish(DomainEvent event) {
    // 1. Payload 직렬화
    String payload = jsonConverter.serialize(event);

    // 2. Envelope 생성
    DomainEventEnvelope envelope = DomainEventEnvelope.of(event, payload);

    // 3. Message 빌드 시 event.topic()과 event.key()를 직접 사용
    var message =
        MessageBuilder.withPayload(envelope)
            .setHeader(KafkaHeaders.TOPIC, event.topic())
            .setHeader(KafkaHeaders.KEY, event.key())
            .setHeader("eventType", envelope.eventType())
            .setHeader("eventId", envelope.eventId())
            .build();

    try {
      /* .get()을 호출하여 비동기 작업을 동기적으로 대기합니다.
        카프카 브로커에 정상적으로 메시지가 전달되어야만 다음 줄로 넘어갑니다.
      */
      kafkaTemplate.send(message).get();

    } catch (InterruptedException | ExecutionException e) {
      log.error(
          "Failed to publish event to Kafka topic: {}, eventType={}, eventId={}",
          event.topic(),
          envelope.eventType(),
          envelope.eventId(),
          e);

      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt(); // 인터럽트 상태 복구
      }

      throw new BusinessException(ErrorStatus.INFRA_KAFKA_PUBLISH_FAILED, e.getMessage());
    }
  }
}
