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
    return performanceCreateUseCase.execute(request);
  }
}
