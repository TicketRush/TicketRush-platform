package com.ticketrush.global.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "1m")
public class ShedLockConfig {

  @Value("${spring.application.name:seat-service}")
  private String applicationName;

  @Value("${spring.profiles.active:default}")
  private String activeProfile;

  @Bean
  public LockProvider lockProvider(RedisConnectionFactory connectionFactory) {
    // 환경에 따라 키가 겹치지 않도록 동적으로 prefix 지정 (예: "seat-service-dev")
    String keyPrefix = applicationName + "-" + activeProfile;
    return new RedisLockProvider(connectionFactory, keyPrefix);
  }
}
