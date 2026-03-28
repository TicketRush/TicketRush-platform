package com.ticketrush.global.event;

import java.time.Instant;
import java.util.UUID;

public record DomainEventEnvelope(
    String eventId,
    String eventType,
    Instant occurredAt,
    String topic,
    String payload,
    String traceId) {

  // 파라미터에서 topic 제거, Object를 DomainEvent로 변경
  public static DomainEventEnvelope of(DomainEvent event, String payload) {
    return new DomainEventEnvelope(
        UUID.randomUUID().toString(),
        event.eventName(),
        Instant.now(),
        event.topic(),
        payload,
        event.traceId());
  }
}
