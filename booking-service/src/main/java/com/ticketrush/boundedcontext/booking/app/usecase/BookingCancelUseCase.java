package com.ticketrush.boundedcontext.booking.app.usecase;

import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import com.ticketrush.boundedcontext.booking.out.repository.BookingRepository;
import com.ticketrush.global.exception.BusinessException;
import com.ticketrush.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookingCancelUseCase {

  private final BookingRepository bookingRepository;

  public void execute(Long bookingId) {
    Booking booking =
        bookingRepository
            .findById(bookingId)
            .orElseThrow(
                () -> {
                  log.warn("예매 취소 실패. 존재하지 않는 bookingId={}", bookingId);
                  return new BusinessException(ErrorStatus.BOOKING_NOT_FOUND);
                });

    booking.cancel();

    log.debug("보상 트랜잭션 정상 처리: 예매가 취소(CANCELED) 되었습니다. bookingId: {}", bookingId);
  }
}
