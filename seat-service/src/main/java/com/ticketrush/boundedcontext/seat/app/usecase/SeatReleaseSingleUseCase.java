package com.ticketrush.boundedcontext.seat.app.usecase;

import com.ticketrush.boundedcontext.seat.out.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatReleaseSingleUseCase {

  private final SeatRepository seatRepository;

  @Transactional
  public void execute(Long seatId) {
    seatRepository
        .findById(seatId)
        .ifPresent(
            seat -> {
              seat.releaseHold();
              log.info("Redis 만료 이벤트 수신: 좌석 {} 상태를 AVAILABLE로 즉시 롤백했습니다.", seatId);
            });
  }
}
