package com.ticketrush.boundedcontext.seat.app.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatUnlockUseCase {

  private final StringRedisTemplate redisTemplate;
  private static final String SEAT_LOCK_PREFIX = "seat:lock:";

  public void execute(Long seatId) {
    String lockKey = SEAT_LOCK_PREFIX + seatId;
    redisTemplate.delete(lockKey);
  }
}
