package com.ticketrush.global.aop;

import java.lang.annotation.Annotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class AopExtractUtils {

  // 인스턴스화 방지
  private AopExtractUtils() {}

  /**
   * 메서드 파라미터 중 특정 어노테이션이 선언된 인자의 값을 추출합니다.
   *
   * @param joinPoint AOP ProceedingJoinPoint
   * @param targetAnnotation 찾고자 하는 어노테이션 클래스 (예: RequestBody.class)
   * @return 어노테이션이 선언된 인자 객체. 없을 경우 null 반환
   */
  public static Object extractArgByAnnotation(
      ProceedingJoinPoint joinPoint, Class<? extends Annotation> targetAnnotation) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
    Object[] args = joinPoint.getArgs();

    for (int i = 0; i < parameterAnnotations.length; i++) {
      for (Annotation annotation : parameterAnnotations[i]) {
        if (annotation.annotationType().equals(targetAnnotation)) {
          return args[i];
        }
      }
    }
    return null;
  }
}
