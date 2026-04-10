package com.ticketrush.boundedcontext.seat.app.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class SeatLockUseCaseTest {

  @InjectMocks private SeatLockUseCase seatLockUseCase;

  @Mock private StringRedisTemplate redisTemplate;

  @Mock private ValueOperations<String, String> valueOperations;

  @Test
  @DisplayName("성공: Redis 락 획득에 성공하면 만료 시간이 담긴 Optional을 반환한다")
  void execute_success_when_lock_acquired() {
    // given
    Long seatId = 100L;
    Long userId = 1L;
    String expectedKey = "seat:lock:" + seatId;

    given(redisTemplate.opsForValue()).willReturn(valueOperations);
    given(
            valueOperations.setIfAbsent(
                eq(expectedKey), eq(String.valueOf(userId)), any(Duration.class)))
        .willReturn(Boolean.TRUE);

    // when
    Optional<LocalDateTime> result = seatLockUseCase.execute(seatId, userId);

    // then
    assertThat(result).isPresent();
    assertThat(result.get()).isAfter(LocalDateTime.now().plusMinutes(4)); // 대략 5분 뒤인지 확인
  }

  @Test
  @DisplayName("실패: 이미 락이 존재하여 획득에 실패하면 빈 Optional을 반환한다")
  void execute_fail_when_lock_already_exists() {
    // given
    Long seatId = 100L;
    Long userId = 1L;
    String expectedKey = "seat:lock:" + seatId;

    given(redisTemplate.opsForValue()).willReturn(valueOperations);
    given(
            valueOperations.setIfAbsent(
                eq(expectedKey), eq(String.valueOf(userId)), any(Duration.class)))
        .willReturn(Boolean.FALSE); // 락 획득 실패 시뮬레이션

    // when
    Optional<LocalDateTime> result = seatLockUseCase.execute(seatId, userId);

    // then
    assertThat(result).isEmpty();
  }
}
