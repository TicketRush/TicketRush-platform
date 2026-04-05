package com.ticketrush.boundedcontext.booking.app.facade;

import com.ticketrush.boundedcontext.booking.app.dto.request.BookingCreateRequest;
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

    // 2. 파라미터들을 묶어 DTO(Record) 객체 생성
    BookingCreateRequest request =
        new BookingCreateRequest(userId, performanceId, seatId, price, bookingNumber);

    // 3. 예약 정보 DB 저장 (DB 트랜잭션 내부) - 생성한 DTO를 전달
    return bookingCreateUseCase.execute(request);
  }
}
