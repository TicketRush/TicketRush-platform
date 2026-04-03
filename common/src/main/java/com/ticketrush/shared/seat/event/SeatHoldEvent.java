package com.ticketrush.shared.seat.event;

import com.ticketrush.global.event.DomainEvent;
import com.ticketrush.global.event.EventUtils;
import java.time.LocalDateTime;

public record SeatHoldEvent(
    Long seatId, Long performanceId, Long userId, LocalDateTime holdExpiredAt)
    implements DomainEvent {

  @Override
  public String topic() {
    return "seat-hold-topic";
  }

  @Override
  public String key() {
    return String.valueOf(performanceId);
  }

  @Override
  public String eventName() {
    return "SeatHoldEvent";
  }

  @Override
  public String traceId() {
    return EventUtils.extractTraceId();
  }
}
