package com.ticketrush.boundedcontext.booking.in.eventlistener;

import com.ticketrush.boundedcontext.booking.app.usecase.BookingCancelUseCase;
import com.ticketrush.global.event.DomainEventEnvelope;
import com.ticketrush.global.json.JsonConverter;
import com.ticketrush.shared.seat.event.SeatHoldFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment; // 추가
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatHoldFailedEventListener {

  private final BookingCancelUseCase bookingCancelUseCase;
  private final JsonConverter jsonConverter;

  @KafkaListener(topics = "seat-hold-failed-topic", groupId = "booking-group")
  public void handleSeatHoldFailed(@Payload DomainEventEnvelope envelope, Acknowledgment ack) {

    SeatHoldFailedEvent event =
        jsonConverter.deserialize(envelope.payload(), SeatHoldFailedEvent.class);

    log.warn(
        "좌석 선점 실패 이벤트 수신. 보상 트랜잭션 실행. bookingId: {}, 사유: {}", event.bookingId(), event.reason());

    bookingCancelUseCase.execute(event.bookingId());

    ack.acknowledge();
  }
}
