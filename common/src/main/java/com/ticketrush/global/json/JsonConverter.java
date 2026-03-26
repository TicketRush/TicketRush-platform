package com.ticketrush.global.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonConverter {

  private final ObjectMapper objectMapper;

  public String serialize(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (Exception e) {
      log.error("Failed to serialize value: {}", value, e);
      throw new SerializationException("failed serialization error", e);
    }
  }

  public <T> T deserialize(String payload, Class<T> type) {
    try {
      return objectMapper.readValue(payload, type);
    } catch (Exception e) {
      log.error("Failed to deserialize payload: {}", payload, e);
      throw new DeserializationException("failed deserialization error", e);
    }
  }

  public <T> T deserialize(String payload, TypeReference<T> type) {
    try {
      return objectMapper.readValue(payload, type);
    } catch (Exception e) {
      log.error("Failed to deserialize payload: {}", payload, e);
      throw new DeserializationException("failed deserialization error", e);
    }
  }
}
