package com.ticketrush.boundedcontext.booking.app.facade;

import com.ticketrush.boundedcontext.booking.app.dto.request.BookingCreateRequest;
import com.ticketrush.boundedcontext.booking.app.usecase.BookingCreateUseCase;
import com.ticketrush.boundedcontext.booking.app.usecase.BookingIssueNumberUseCase;
import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import com.ticketrush.global.eventpublisher.EventPublisher;
import com.ticketrush.global.exception.BusinessException;
import com.ticketrush.global.status.ErrorStatus;
import com.ticketrush.shared.booking.event.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingFacade {

  private final BookingIssueNumberUseCase bookingIssueNumberUseCase;
  private final BookingCreateUseCase bookingCreateUseCase;
  private final EventPublisher eventPublisher;

  @Transactional // DB 저장과 이벤트 발행을 하나의 트랜잭션으로 묶음
  public Booking createBooking(Long userId, Long performanceId, Long seatId) {
    // 1. 고유 예약 번호 발급
    String bookingNumber = bookingIssueNumberUseCase.execute();

    // 2. PENDING 상태로 예매 생성
    BookingCreateRequest request =
        new BookingCreateRequest(userId, performanceId, seatId, bookingNumber);
    Booking savedBooking = bookingCreateUseCase.execute(request);

    // 3. 트랜잭션 성공 후 "가주문 생성됨" 이벤트 발행
    try {
      BookingCreatedEvent event =
          new BookingCreatedEvent(savedBooking.getId(), seatId, performanceId, userId);
      eventPublisher.publish(event);
    } catch (Exception e) {
      log.error("Kafka 이벤트 발행 실패로 인해 예매 생성을 롤백합니다. bookingId: {}", savedBooking.getId(), e);
      // 예외를 발생시켜 @Transactional에 의한 DB 롤백을 유도합니다.
      throw new BusinessException(
          ErrorStatus.INTERNAL_SERVER_ERROR, "예매 처리 중 이벤트 발행에 실패하여 취소되었습니다.");
    }

    return savedBooking;
  }
}
