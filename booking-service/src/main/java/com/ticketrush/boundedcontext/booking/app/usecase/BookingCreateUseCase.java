package com.ticketrush.boundedcontext.booking.app.usecase;

import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import com.ticketrush.boundedcontext.booking.domain.types.BookingStatus;
import com.ticketrush.boundedcontext.booking.out.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingCreateUseCase {

  private final BookingRepository bookingRepository;

  public Booking execute(
      Long userId, Long performanceId, Long seatId, Long price, String bookingNumber) {
    Booking booking =
        Booking.builder()
            .bookingNumber(bookingNumber)
            .userId(userId)
            .performanceId(performanceId)
            .seatId(seatId)
            .price(price)
            .bookingStatus(BookingStatus.PENDING)
            .build();

    return bookingRepository.save(booking);
  }
}
