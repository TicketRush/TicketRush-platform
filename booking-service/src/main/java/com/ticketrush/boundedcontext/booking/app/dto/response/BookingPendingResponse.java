package com.ticketrush.boundedcontext.booking.app.dto.response;

import com.ticketrush.boundedcontext.booking.domain.entity.Booking;

public record BookingPendingResponse(Long bookingId, String bookingNumber, String status) {
  public static BookingPendingResponse from(Booking booking) {
    return new BookingPendingResponse(
        booking.getId(), booking.getBookingNumber(), booking.getBookingStatus().name());
  }
}
