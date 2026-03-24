package com.ticketrush.boundedcontext.performance.domain.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PerformanceStatus {
  UPCOMING("판매 예정"),
  ON_SALE("판매 중"),
  CLOSED("판매 종료"),
  CANCELED("취소");

  private final String description;
}
