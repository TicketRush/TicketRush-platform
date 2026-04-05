package com.ticketrush.boundedcontext.seat.in.eventlistener;

import com.ticketrush.boundedcontext.seat.app.facade.SeatFacade;
import com.ticketrush.global.event.DomainEventEnvelope;
import com.ticketrush.global.json.JsonConverter;
import com.ticketrush.shared.booking.event.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCreatedEventListener {

  private final SeatFacade seatFacade;
  private final JsonConverter jsonConverter;

  @KafkaListener(topics = "booking-created-topic", groupId = "seat-group")
  public void handleBookingCreated(@Payload DomainEventEnvelope envelope) {
    BookingCreatedEvent event =
        jsonConverter.deserialize(envelope.payload(), BookingCreatedEvent.class);

    seatFacade.tryLockSeat(event.bookingId(), event.seatId(), event.userId());
  }
}
