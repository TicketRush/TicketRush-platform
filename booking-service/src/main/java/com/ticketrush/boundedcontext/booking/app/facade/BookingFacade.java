package com.ticketrush.boundedcontext.booking.app.facade;

import com.ticketrush.boundedcontext.booking.app.dto.request.BookingCreateRequest;
import com.ticketrush.boundedcontext.booking.app.usecase.BookingCreateUseCase;
import com.ticketrush.boundedcontext.booking.app.usecase.BookingIssueNumberUseCase;
import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import com.ticketrush.global.eventpublisher.EventPublisher;
import com.ticketrush.shared.booking.event.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingFacade {

  private final BookingIssueNumberUseCase bookingIssueNumberUseCase;
  private final BookingCreateUseCase bookingCreateUseCase;
  private final EventPublisher eventPublisher;

  public Booking createBooking(Long userId, Long performanceId, Long seatId, Long price) {
    // 1. 고유 예약 번호 발급
    String bookingNumber = bookingIssueNumberUseCase.execute();

    // 2. 일단 PENDING 상태로 예매 생성 (트랜잭션)
    BookingCreateRequest request =
        new BookingCreateRequest(userId, performanceId, seatId, price, bookingNumber);
    Booking savedBooking = bookingCreateUseCase.execute(request);

    // 3. 트랜잭션 성공 후 "가주문 생성됨" 이벤트 발행
    BookingCreatedEvent event =
        new BookingCreatedEvent(savedBooking.getId(), seatId, performanceId, userId);
    eventPublisher.publish(event);

    return savedBooking;
  }
}
