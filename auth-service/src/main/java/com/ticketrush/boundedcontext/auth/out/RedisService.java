package com.ticketrush.boundedcontext.auth.out;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

  private final RedisTemplate<String, String> redisTemplate;

  // 저장
  public void saveRefreshToken(Long userId, String refreshToken, long expiration) {
    String key = "RT:" + userId;
    redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expiration));
  }

  // 조회
  public String getRefreshToken(Long userId) {
    return redisTemplate.opsForValue().get("RT:" + userId);
  }

  // 삭제 (로그아웃)
  public void deleteRefreshToken(Long userId) {
    redisTemplate.delete("RT:" + userId);
  }

  // 검증
  public boolean isValidRefreshToken(Long userId, String refreshToken) {
    String saved = getRefreshToken(userId);

    System.out.println("요청 토큰 길이 = " + refreshToken.length());
    System.out.println("저장 토큰 길이 = " + (saved != null ? saved.length() : 0));

    System.out.println("요청 토큰 = [" + refreshToken + "]");
    System.out.println("저장 토큰 = [" + saved + "]");

    return saved != null && saved.equals(refreshToken);
  }
}
