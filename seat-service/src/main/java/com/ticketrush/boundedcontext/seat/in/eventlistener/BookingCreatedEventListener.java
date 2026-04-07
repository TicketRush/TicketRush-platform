package com.ticketrush.boundedcontext.seat.in.eventlistener;

import com.ticketrush.boundedcontext.seat.app.facade.SeatFacade;
import com.ticketrush.global.event.DomainEventEnvelope;
import com.ticketrush.global.json.JsonConverter;
import com.ticketrush.shared.booking.event.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCreatedEventListener {

  private final SeatFacade seatFacade;
  private final JsonConverter jsonConverter;

  @KafkaListener(topics = "booking-created-topic", groupId = "seat-group")
  public void handleBookingCreated(@Payload DomainEventEnvelope envelope, Acknowledgment ack) {

    BookingCreatedEvent event =
        jsonConverter.deserialize(envelope.payload(), BookingCreatedEvent.class);

    // 1. 좌석 선점 시도 (실패하거나 예외가 터지면 상위로 전파됨)
    seatFacade.tryLockSeat(event.bookingId(), event.seatId(), event.userId());

    // 2. 예외 없이 로직이 성공적으로 완료되었을 때만 오프셋 수동 커밋
    ack.acknowledge();
  }
}
