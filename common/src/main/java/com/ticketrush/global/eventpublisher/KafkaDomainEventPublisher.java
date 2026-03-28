package com.ticketrush.global.eventpublisher;

import com.ticketrush.global.event.DomainEvent;
import com.ticketrush.global.event.DomainEventEnvelope;
import com.ticketrush.global.json.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnExpression("'${app.event-publisher.type}' == 'kafka'")
@RequiredArgsConstructor
public class KafkaDomainEventPublisher implements EventPublisher {

  private final KafkaTemplate<String, DomainEventEnvelope> kafkaTemplate;
  private final JsonConverter jsonConverter;

  @Override
  public void publish(DomainEvent event) {
    // 1. Payload 직렬화
    String payload = jsonConverter.serialize(event);

    // 2. Envelope 생성
    DomainEventEnvelope envelope = DomainEventEnvelope.of(event, payload);

    // 3. Message 빌드 시 event.topic()과 event.key()를 직접 사용
    var message =
        MessageBuilder.withPayload(envelope)
            .setHeader(KafkaHeaders.TOPIC, event.topic())
            .setHeader(KafkaHeaders.KEY, event.key())
            .setHeader("eventType", envelope.eventType())
            .setHeader("eventId", envelope.eventId())
            .build();

    kafkaTemplate
        .send(message)
        .whenComplete(
            (result, ex) -> {
              if (ex != null) {
                log.error(
                    "Failed to publish event to Kafka topic: {}, eventType={}, eventId={}",
                    event.topic(),
                    envelope.eventType(),
                    envelope.eventId(),
                    ex);
              }
            });
  }
}
