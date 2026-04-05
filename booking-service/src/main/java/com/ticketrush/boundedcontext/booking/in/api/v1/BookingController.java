package com.ticketrush.boundedcontext.booking.in.api.v1;

import com.ticketrush.boundedcontext.booking.app.dto.request.BookingPendingRequest;
import com.ticketrush.boundedcontext.booking.app.dto.response.BookingPendingResponse;
import com.ticketrush.boundedcontext.booking.app.facade.BookingFacade;
import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Booking API", description = "예약 관련 API")
@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

  private final BookingFacade bookingFacade;

  @Operation(
      summary = "예매 생성 (결제 대기 상태)",
      description = "사용자가 공연 좌석을 선택하면 예매를 생성하고 상태를 PENDING으로 설정합니다.")
  @PostMapping("/pending")
  public ResponseEntity<BookingPendingResponse> createPendingBooking(
      @Valid @RequestBody BookingPendingRequest request) {

    Booking booking =
        bookingFacade.createBooking(request.userId(), request.performanceId(), request.seatId());

    return ResponseEntity.status(HttpStatus.CREATED).body(BookingPendingResponse.from(booking));
  }
}
