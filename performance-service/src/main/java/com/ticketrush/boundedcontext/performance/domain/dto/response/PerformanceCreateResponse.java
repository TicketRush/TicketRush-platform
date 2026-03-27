package com.ticketrush.boundedcontext.performance.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceCreateResponse {
  private Long performanceId; // 생성된 공연의 고유 ID
}
