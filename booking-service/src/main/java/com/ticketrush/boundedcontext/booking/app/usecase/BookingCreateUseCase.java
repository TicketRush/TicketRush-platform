package com.ticketrush.boundedcontext.booking.app.usecase;

import com.ticketrush.boundedcontext.booking.app.dto.request.BookingCreateRequest;
import com.ticketrush.boundedcontext.booking.app.mapper.BookingMapper;
import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import com.ticketrush.boundedcontext.booking.out.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingCreateUseCase {

  private final BookingRepository bookingRepository;
  private final BookingMapper bookingMapper;

  public Booking execute(BookingCreateRequest request) {
    Booking booking = bookingMapper.toEntity(request);
    return bookingRepository.save(booking);
  }
}
