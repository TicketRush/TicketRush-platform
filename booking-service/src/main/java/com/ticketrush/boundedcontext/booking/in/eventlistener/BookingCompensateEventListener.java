package com.ticketrush.boundedcontext.booking.in.eventlistener;

import com.ticketrush.boundedcontext.booking.app.usecase.BookingCancelUseCase;
import com.ticketrush.global.event.DomainEventEnvelope;
import com.ticketrush.global.json.JsonConverter;
import com.ticketrush.shared.seat.event.SeatHoldFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCompensateEventListener {

  private final BookingCancelUseCase bookingCancelUseCase;
  private final JsonConverter jsonConverter;

  @KafkaListener(topics = "seat-hold-failed-topic", groupId = "booking-group")
  public void handleSeatHoldFailed(@Payload DomainEventEnvelope envelope) {
    SeatHoldFailedEvent event =
        jsonConverter.deserialize(envelope.payload(), SeatHoldFailedEvent.class);

    log.warn(
        "좌석 선점 실패 이벤트 수신. 보상 트랜잭션 실행. bookingId: {}, 사유: {}", event.bookingId(), event.reason());

    try {
      bookingCancelUseCase.execute(event.bookingId());
    } catch (Exception e) {
      log.error("보상 트랜잭션(예매 취소) 처리 중 시스템 오류 발생. bookingId: {}", event.bookingId(), e);
    }
  }
}
