package com.ticketrush.boundedcontext.seat.app.scheduler;

import com.ticketrush.boundedcontext.seat.app.usecase.SeatReleaseExpiredUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatStatusScheduler {

  private final SeatReleaseExpiredUseCase seatReleaseExpiredUseCase;

  // 실시간 처리는 Redis Event Listener가 담당하므로, 스케줄러는 1분(60,000ms) 주기의 Fallback 역할만 수행
  // TODO: 다중 인스턴스 배포(Scale-Out) 시 중복 벌크 업데이트 방지를 위해 ShedLock 또는 Redis 분산 락 도입 필요
  @Scheduled(fixedDelay = 60000)
  public void scheduleReleaseExpiredHoldsFallback() {
    log.debug("Fallback 스케줄러 동작: 유실된 만료 좌석 검사 시작");
    seatReleaseExpiredUseCase.execute();
  }
}
