package com.ticketrush.boundedcontext.booking.app.facade;

import com.ticketrush.boundedcontext.booking.app.dto.request.BookingCreateRequest;
import com.ticketrush.boundedcontext.booking.app.usecase.BookingCreateUseCase;
import com.ticketrush.boundedcontext.booking.app.usecase.BookingIssueNumberUseCase;
import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingFacade {

  private final BookingIssueNumberUseCase bookingIssueNumberUseCase;
  private final BookingCreateUseCase bookingCreateUseCase;

  public Booking createBooking(Long userId, Long performanceId, Long seatId) {
    // 1. 고유 예약 번호 발급
    String bookingNumber = bookingIssueNumberUseCase.execute();

    // 2. PENDING 상태로 예매 생성
    BookingCreateRequest request =
        new BookingCreateRequest(userId, performanceId, seatId, bookingNumber);

    return bookingCreateUseCase.execute(request);
  }
}
