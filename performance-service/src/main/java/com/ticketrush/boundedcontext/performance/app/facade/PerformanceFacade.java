package com.ticketrush.boundedcontext.performance.app.facade;

import com.ticketrush.boundedcontext.performance.app.usecase.PerformanceCreateUseCase;
import com.ticketrush.boundedcontext.performance.domain.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.domain.dto.response.PerformanceCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceFacade {

  private final PerformanceCreateUseCase performanceCreateUseCase;

  public PerformanceCreateResponse createPerformance(PerformanceCreateRequest request) {
    // UseCase의 execute를 호출하여 ID를 받고 응답 객체로 변환
    Long performanceId = performanceCreateUseCase.execute(request);
    return new PerformanceCreateResponse(performanceId);
  }
}
