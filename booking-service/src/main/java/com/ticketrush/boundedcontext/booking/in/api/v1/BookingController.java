package com.ticketrush.boundedcontext.booking.in.api.v1;

import com.ticketrush.boundedcontext.booking.app.dto.request.BookingPendingRequest;
import com.ticketrush.boundedcontext.booking.app.dto.response.BookingPendingResponse;
import com.ticketrush.boundedcontext.booking.app.facade.BookingFacade;
import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

  private final BookingFacade bookingFacade;

  @PostMapping("/pending")
  public ResponseEntity<BookingPendingResponse> createPendingBooking(
      @Valid @RequestBody BookingPendingRequest request) {

    Booking booking =
        bookingFacade.createBooking(
            request.userId(), request.performanceId(), request.seatId(), request.price());

    return ResponseEntity.status(HttpStatus.CREATED).body(BookingPendingResponse.from(booking));
  }
}
