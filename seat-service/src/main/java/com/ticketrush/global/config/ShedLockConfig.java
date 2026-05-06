package com.ticketrush.global.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "1m")
public class ShedLockConfig {

  @Bean
  public LockProvider lockProvider(RedisConnectionFactory connectionFactory) {
    // 환경에 따라 키가 겹치지 않도록 prefix를 지정합니다.
    return new RedisLockProvider(connectionFactory, "seat-env");
  }
}
