package com.ticketrush.boundedcontext.booking.in.eventlistener;

import com.ticketrush.boundedcontext.booking.app.usecase.BookingCancelUseCase;
import com.ticketrush.global.event.DomainEventEnvelope;
import com.ticketrush.global.json.JsonConverter;
import com.ticketrush.shared.seat.event.SeatHoldFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatHoldFailedEventListener {

  private final BookingCancelUseCase bookingCancelUseCase;
  private final JsonConverter jsonConverter;

  @KafkaListener(topics = "seat-hold-failed-topic", groupId = "booking-group")
  public void handleSeatHoldFailed(@Payload DomainEventEnvelope envelope, Acknowledgment ack) {

    SeatHoldFailedEvent event = null;

    try {
      event = jsonConverter.deserialize(envelope.payload(), SeatHoldFailedEvent.class);

      log.warn(
          "좌석 선점 실패 이벤트 수신. 보상 트랜잭션 실행. bookingId: {}, 사유: {}", event.bookingId(), event.reason());

      bookingCancelUseCase.execute(event.bookingId());

    } catch (Exception e) {
      // 에러가 났을 때 로그를 강하게 남겨 수동 복구가 가능하도록 조치
      Long failedBookingId = (event != null) ? event.bookingId() : null;
      log.error(
          "[CRITICAL] 좌석 선점 실패에 대한 예매 취소 중 치명적 오류 발생! "
              + "데이터 정합성이 깨졌습니다. 확인이 필요합니다. bookingId: {}",
          failedBookingId,
          e);

      // TODO: 추후 고도화 시, 이 위치에서 DLT로 실패한 이벤트를 재발행

    } finally {
      // 카프카 메시지가 무한 반복되며 파티션을 막는 현상 방지
      ack.acknowledge();
    }
  }
}
