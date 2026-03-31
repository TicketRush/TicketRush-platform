package com.ticketrush.boundedcontext.booking.app.facade;

import com.ticketrush.boundedcontext.booking.app.usecase.BookingCreateUseCase;
import com.ticketrush.boundedcontext.booking.app.usecase.BookingIssueNumberUseCase;
import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingFacade {

  private final BookingIssueNumberUseCase bookingIssueNumberUseCase;
  private final BookingCreateUseCase bookingCreateUseCase;

  public Booking createBooking(Long userId, Long performanceId, Long seatId, Long price) {
    // 1. 고유 예약 번호 발급 (Redis 통신 - 트랜잭션 외부)
    String bookingNumber = bookingIssueNumberUseCase.execute();

    // 2. 예약 정보 DB 저장 (DB 트랜잭션 내부)
    return bookingCreateUseCase.execute(userId, performanceId, seatId, price, bookingNumber);
  }
}
