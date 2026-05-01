package com.ticketrush.boundedcontext.booking.in.eventlistener;

import com.ticketrush.global.eventpublisher.EventPublisher;
import com.ticketrush.global.exception.BusinessException;
import com.ticketrush.global.status.ErrorStatus;
import com.ticketrush.shared.booking.event.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCreatedEventListener {
  private final EventPublisher eventPublisher;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleBookingCreatedEvent(BookingCreatedEvent event) {
    try {
      eventPublisher.publish(event);
    } catch (Exception e) {
      log.error("Kafka 이벤트 발행 실패. bookingId: {}", event.bookingId(), e);

      throw new BusinessException(ErrorStatus.EVENT_PUBLISH_ERROR, e);
    }
  }
}
