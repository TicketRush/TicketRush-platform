package com.ticketrush.global.config;

import com.ticketrush.global.kafka.TopicNames;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

@Configuration
@ConditionalOnExpression(
    "'${app.event-publisher.type}' == 'kafka' or '${app.event-publisher.type}' == 'outbox'")
public class KafkaTopicConfig {

  @Bean
  public NewTopics performanceKafkaTopics() {
    return new NewTopics(
        TopicBuilder.name(TopicNames.PERFORMANCE_EVENTS).partitions(1).replicas(1).build());
  }
}
