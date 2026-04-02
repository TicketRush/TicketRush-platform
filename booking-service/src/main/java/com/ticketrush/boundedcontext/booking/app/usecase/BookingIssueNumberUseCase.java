package com.ticketrush.boundedcontext.booking.app.usecase;

import com.ticketrush.boundedcontext.booking.domain.policy.BookingNumberGenerator;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingIssueNumberUseCase {

  private final StringRedisTemplate redisTemplate;
  private static final String REDIS_KEY_PREFIX = "booking:number:";
  private static final int MAX_RETRY_COUNT = 5; // 중복 발생 시 최대 재시도 횟수

  /**
   * 고유한 예약 번호를 발급합니다.
   *
   * @return 생성된 고유 예약 번호
   */
  public String execute() {
    for (int i = 0; i < MAX_RETRY_COUNT; i++) {
      String generatedNumber = BookingNumberGenerator.generate();
      String redisKey = REDIS_KEY_PREFIX + generatedNumber;

      /* Redis SETNX를 이용한 원자적 중복 체크
       예약 번호가 DB에 저장될 때까지만 임시로 잡아두면 되므로 TTL을 짧게(10분) 설정
      */
      Boolean isUnique =
          redisTemplate.opsForValue().setIfAbsent(redisKey, "RESERVED", Duration.ofMinutes(10));

      if (Boolean.TRUE.equals(isUnique)) {
        return generatedNumber; // 고유 번호 발급 성공
      }

      log.warn("예약 번호 충돌 발생, 재시도합니다. (시도 횟수: {})", i + 1);
    }

    throw new IllegalStateException("고유한 예약 번호를 생성할 수 없습니다. 잠시 후 다시 시도해주세요.");
  }
}
