package com.ticketrush.boundedcontext.booking.app.facade;

import com.ticketrush.boundedcontext.booking.app.usecase.BookingCreateUseCase;
import com.ticketrush.boundedcontext.booking.app.usecase.BookingIssueNumberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingFacade {

  private final BookingIssueNumberUseCase bookingIssueNumberUseCase;
  private final BookingCreateUseCase bookingCreateUseCase;

  public String issueNumber() {
    return bookingIssueNumberUseCase.execute();
  }

  public void createBooking(
      Long userId, Long performanceId, Long seatId, Long price, String bookingNumber) {
    bookingCreateUseCase.execute(userId, performanceId, seatId, price, bookingNumber);
  }
}
