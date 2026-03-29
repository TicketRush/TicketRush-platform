package com.ticketrush.global.aop;

import com.ticketrush.global.constants.TraceIdConstants;
import com.ticketrush.global.event.DomainEventEnvelope;
import com.ticketrush.global.json.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaLoggingAspect {

  private final JsonConverter jsonConverter;

  private static final int MAX_LOG_LENGTH = 1000;
  private static final String UNKNOWN_TRACE_ID = "Unknown";

  @Pointcut("@annotation(org.springframework.kafka.annotation.KafkaListener)")
  public void kafkaListenerPointcut() {}

  @Around("kafkaListenerPointcut()")
  public Object logKafkaEvent(ProceedingJoinPoint joinPoint) throws Throwable {
    String targetClass = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    long startTime = System.currentTimeMillis();

    // Kafka 메시지(Envelope)에서 Trace ID 추출 및 MDC 주입
    String eventTraceId = extractTraceId(args);
    String originalTraceId = MDC.get(TraceIdConstants.TRACE_ID_KEY);
    boolean mdcModified = false;

    if (StringUtils.hasText(eventTraceId) && !eventTraceId.equals(UNKNOWN_TRACE_ID)) {
      MDC.put(TraceIdConstants.TRACE_ID_KEY, eventTraceId);
      mdcModified = true;
    }

    String payload =
        (args != null && args.length > 0)
            ? jsonConverter.serializeForLog(args[0], MAX_LOG_LENGTH)
            : "none";
    log.info("[KAFKA CONSUME START] {}.{} | Payload: {}", targetClass, methodName, payload);

    try {
      Object result = joinPoint.proceed();
      long elapsedTime = System.currentTimeMillis() - startTime;

      log.info("[KAFKA CONSUME SUCCESS] {}.{} | Time: {}ms", targetClass, methodName, elapsedTime);
      return result;

    } catch (Exception e) {
      long elapsedTime = System.currentTimeMillis() - startTime;
      log.error(
          "[KAFKA CONSUME FAILED] {}.{} | Time: {}ms | Error: {}",
          targetClass,
          methodName,
          elapsedTime,
          e.getMessage(),
          e);
      throw e;

    } finally {
      // 스레드 풀 오염을 방지하기 위한 MDC 복구 처리
      if (mdcModified) {
        if (originalTraceId != null) {
          MDC.put(TraceIdConstants.TRACE_ID_KEY, originalTraceId);
        } else {
          MDC.remove(TraceIdConstants.TRACE_ID_KEY);
        }
      }
    }
  }

  // 인자에서 Trace ID 추출
  private String extractTraceId(Object[] args) {
    if (args == null || args.length == 0) {
      return UNKNOWN_TRACE_ID;
    }

    if (args[0] instanceof DomainEventEnvelope envelope
        && StringUtils.hasText(envelope.traceId())) {
      return envelope.traceId();
    }

    return UNKNOWN_TRACE_ID;
  }
}
