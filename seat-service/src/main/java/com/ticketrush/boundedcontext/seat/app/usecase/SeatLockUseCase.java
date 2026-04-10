package com.ticketrush.boundedcontext.seat.app.usecase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatLockUseCase {

  private final StringRedisTemplate redisTemplate;

  private static final String SEAT_LOCK_PREFIX = "seat:lock:";
  private static final int LOCK_TTL_MINUTES = 5;

  public Optional<LocalDateTime> execute(Long seatId, Long userId) {
    String lockKey = SEAT_LOCK_PREFIX + seatId;

    // 1. Redis 락 시도
    Boolean isLocked =
        redisTemplate
            .opsForValue()
            .setIfAbsent(lockKey, String.valueOf(userId), Duration.ofMinutes(LOCK_TTL_MINUTES));

    // 2. 결과에 따라 만료 시간 반환 또는 빈 객체 반환
    if (Boolean.TRUE.equals(isLocked)) {
      return Optional.of(LocalDateTime.now().plusMinutes(LOCK_TTL_MINUTES));
    }

    return Optional.empty();
  }
}
