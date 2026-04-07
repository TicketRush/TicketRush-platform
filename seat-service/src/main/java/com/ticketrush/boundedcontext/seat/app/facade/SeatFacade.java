package com.ticketrush.boundedcontext.seat.app.facade;

import com.ticketrush.boundedcontext.seat.app.dto.response.SeatLayoutResponse;
import com.ticketrush.boundedcontext.seat.app.usecase.SeatGetSeatLayoutsUseCase;
import com.ticketrush.boundedcontext.seat.app.usecase.SeatHoldUseCase;
import com.ticketrush.boundedcontext.seat.app.usecase.SeatLockUseCase;
import com.ticketrush.boundedcontext.seat.app.usecase.SeatUnlockUseCase;
import com.ticketrush.global.eventpublisher.EventPublisher;
import com.ticketrush.shared.seat.event.SeatHoldFailedEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatFacade {

  private final SeatGetSeatLayoutsUseCase seatGetSeatLayoutsUseCase;
  private final SeatHoldUseCase seatHoldUseCase;
  private final SeatLockUseCase seatLockUseCase;
  private final SeatUnlockUseCase seatUnlockUseCase;
  private final EventPublisher eventPublisher;

  public List<SeatLayoutResponse> getPerformanceSeatLayouts(Long performanceId) {
    return seatGetSeatLayoutsUseCase.execute(performanceId);
  }

  public void holdSeat(Long seatId, LocalDateTime holdExpiredAt) {
    seatHoldUseCase.execute(seatId, holdExpiredAt);
  }

  public void tryLockSeat(Long bookingId, Long seatId, Long userId) {
    // 1. Redis 락 시도
    Optional<LocalDateTime> holdExpiredAtOpt = seatLockUseCase.execute(seatId, userId);

    if (holdExpiredAtOpt.isPresent()) {
      try {
        // 2-A. 성공: Seat DB 상태를 HOLD로 업데이트
        seatHoldUseCase.execute(seatId, holdExpiredAtOpt.get());

      } catch (Exception e) {
        log.error("좌석 DB 업데이트 중 오류 발생. Redis 락 해제 및 보상 이벤트 발행. seatId: {}", seatId, e);

        // 1) 획득했던 Redis 락 즉시 해제
        seatUnlockUseCase.execute(seatId);

        // 2) Booking 모듈로 보상 트랜잭션 이벤트 발행
        SeatHoldFailedEvent failedEvent =
            new SeatHoldFailedEvent(bookingId, seatId, "좌석 상태 업데이트 실패: " + e.getMessage());
        eventPublisher.publish(failedEvent);
      }
    } else {
      // 2-B. 실패: Booking 모듈로 보상 트랜잭션 이벤트 발행
      SeatHoldFailedEvent failedEvent = new SeatHoldFailedEvent(bookingId, seatId, "이미 선점된 좌석입니다.");
      eventPublisher.publish(failedEvent);
    }
  }
}
