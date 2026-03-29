package com.ticketrush.global.aop;

import com.ticketrush.global.json.JsonConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

  private final JsonConverter jsonConverter;

  private static final int MAX_LOG_LENGTH = 1000;

  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  public void restControllerPointcut() {}

  @Around("restControllerPointcut()")
  public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    // HTTP 요청 컨텍스트가 없는 경우 (예: 내부 스케줄러 등) 바로 진행
    if (attributes == null) {
      return joinPoint.proceed();
    }

    HttpServletRequest request = attributes.getRequest();
    String method = request.getMethod();
    String requestURI = request.getRequestURI();
    long startTime = System.currentTimeMillis();

    // 1. 요청 로깅
    Object[] args = joinPoint.getArgs();
    String requestBody =
        (args != null && args.length > 0)
            ? jsonConverter.serializeForLog(args[0], MAX_LOG_LENGTH)
            : "none";
    log.info("[API REQUEST] [{}] {} | Payload: {}", method, requestURI, requestBody);

    try {
      // 2. 실제 컨트롤러 메서드 실행
      Object result = joinPoint.proceed();
      long elapsedTime = System.currentTimeMillis() - startTime;

      // 응답 상태 코드 추출
      HttpServletResponse response = attributes.getResponse();
      int status = (response != null) ? response.getStatus() : 200;

      // 3. 정상 응답 로깅
      log.info(
          "[API RESPONSE] [{}] {} | Status: {} | Time: {}ms",
          method,
          requestURI,
          status,
          elapsedTime);
      return result;

    } catch (Exception e) {
      // 4. 예외 발생 시 로깅
      long elapsedTime = System.currentTimeMillis() - startTime;
      log.error(
          "[API ERROR] [{}] {} | Time: {}ms | Exception: {}",
          method,
          requestURI,
          elapsedTime,
          e.getClass().getSimpleName(),
          e);
      throw e; // GlobalExceptionHandler 처리를 위해 예외를 다시 던짐
    }
  }
}
