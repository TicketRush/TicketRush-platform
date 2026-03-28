package com.ticketrush.global.eventpublisher.topic;

import com.ticketrush.global.event.DomainEvent;
import com.ticketrush.global.event.EventUtils;
import java.time.Instant;
import java.util.UUID;

public record DomainEventEnvelope(
    String eventId,
    String eventType,
    Instant occurredAt,
    String topic,
    String payload,
    String traceId) {

  public static DomainEventEnvelope of(Object event, String topic, String payload) {
    String traceId = extractTraceId(event);
    String eventType = event.getClass().getSimpleName();
    return new DomainEventEnvelope(
        UUID.randomUUID().toString(), eventType, Instant.now(), topic, payload, traceId);
  }

  private static String extractTraceId(Object event) {
    if (event instanceof DomainEvent domainEvent) {
      return domainEvent.traceId();
    }
    return EventUtils.extractTraceId();
  }
}
