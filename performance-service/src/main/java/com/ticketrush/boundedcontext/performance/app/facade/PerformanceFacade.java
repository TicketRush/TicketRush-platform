package com.ticketrush.boundedcontext.performance.app.facade;

import com.ticketrush.boundedcontext.performance.app.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.app.dto.response.PerformanceCreateResponse;
import com.ticketrush.boundedcontext.performance.app.usecase.PerformanceCreateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceFacade {

  private final PerformanceCreateUseCase performanceCreateUseCase;

  public PerformanceCreateResponse createPerformance(PerformanceCreateRequest request) {
    return performanceCreateUseCase.execute(request);
  }
}
