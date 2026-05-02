package com.ticketrush.boundedcontext.seat.app.scheduler;

import com.ticketrush.boundedcontext.seat.app.usecase.SeatReleaseExpiredUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatStatusScheduler {

  private final SeatReleaseExpiredUseCase seatReleaseExpiredUseCase;

  /** 10초마다 만료된 좌석을 검사하여 롤백 (트래픽 상황에 맞춰 주기 조정) */
  @Scheduled(fixedDelay = 10000)
  public void scheduleReleaseExpiredHolds() {
    seatReleaseExpiredUseCase.execute();
  }
}
