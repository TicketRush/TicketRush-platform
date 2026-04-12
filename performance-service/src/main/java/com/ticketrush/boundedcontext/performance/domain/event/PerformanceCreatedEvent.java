package com.ticketrush.boundedcontext.performance.domain.event;

import com.ticketrush.global.event.DomainEvent;
import com.ticketrush.global.event.EventUtils;
import java.time.LocalDate;
import java.time.LocalTime;

public record PerformanceCreatedEvent(
    Long performanceId,
    String title,
    Integer totalSeats,
    LocalDate showDate,
    LocalTime showTime,
    Long price)
    implements DomainEvent {

  @Override
  public String topic() {
    return "performance-events";
  }

  @Override
  public String key() {
    return String.valueOf(performanceId);
  }

  @Override
  public String eventName() {
    return "PerformanceCreated";
  }

  @Override
  public String traceId() {
    return EventUtils.extractTraceId();
  }
}
