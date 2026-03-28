package com.ticketrush.global.eventpublisher.topic;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression(
    "'${app.event-publisher.type}' == 'kafka' or '${app.event-publisher.type}' == 'outbox'")
public class KafkaResolver {

  private static final String AUTH_EVENTS_TOPIC = "auth-events";
  private static final String BOOKING_EVENTS_TOPIC = "booking-events";
  private static final String PAYMENT_EVENTS_TOPIC = "payment-events";
  private static final String PERFORMANCE_EVENTS_TOPIC = "performance-events";
  private static final String SEAT_EVENTS_TOPIC = "seat-events";
  private static final String TICKET_EVENTS_TOPIC = "ticket-events";
  private static final String USER_EVENTS_TOPIC = "user-events";

  // TODO: topic Resolver 를 각 모듈로 변경할지, kafka 에서 관리할지 고민
  public KafkaPublishTarget resolve(Object event) {
    return switch (event) {
      // 추후 각 모듈별 이벤트 추가

      // default
      default -> KafkaPublishTarget.of("unexpected-events-topic", "Unknown", "unexpected-key");
    };
  }
}
