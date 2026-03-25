package com.ticketrush.global.event;

public interface DomainEvent {
  // 1. 라우팅 정보 (Kafka 전송 시 사용)
  String topic();

  String key();

  // 2. 메타데이터 (로깅, 추적, Outbox 저장 시 사용)
  String eventName();

  String traceId();
}
