package com.ticketrush.boundedcontext.seat.in.eventlistener;

import com.ticketrush.boundedcontext.seat.app.facade.SeatFacade;
import com.ticketrush.global.event.DomainEventEnvelope;
import com.ticketrush.global.json.JsonConverter;
import com.ticketrush.shared.seat.event.SeatHoldEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatHoldEventListener {

  private final SeatFacade seatFacade;
  private final JsonConverter jsonConverter;

  @KafkaListener(topics = "seat-hold-topic", groupId = "seat-group")
  public void handleSeatHoldEvent(@Payload DomainEventEnvelope envelope) {
    // 1. Envelope 내부의 JSON 문자열(payload)을 실제 도메인 이벤트 객체로 역직렬화
    SeatHoldEvent event = jsonConverter.deserialize(envelope.payload(), SeatHoldEvent.class);

    log.info(
        "Kafka Event Received - Seat Hold: seatId={}, expiredAt={}, traceId={}",
        event.seatId(),
        event.holdExpiredAt(),
        envelope.traceId());

    try {
      // 2. Facade를 통해 좌석 상태 변경 로직 실행
      seatFacade.holdSeat(event.seatId(), event.holdExpiredAt());
    } catch (Exception e) {
      log.error(
          "좌석 임시 선점 처리 중 오류 발생. seatId: {}, traceId: {}", event.seatId(), envelope.traceId(), e);
    }
  }
}
