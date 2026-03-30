package com.ticketrush.boundedcontext.performance.app.usecase;

import com.ticketrush.boundedcontext.performance.app.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.app.dto.response.PerformanceCreateResponse;
import com.ticketrush.boundedcontext.performance.domain.entity.Performance;
import com.ticketrush.boundedcontext.performance.out.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PerformanceCreateUseCase {

  private final PerformanceRepository performanceRepository;

  public PerformanceCreateResponse execute(PerformanceCreateRequest request) {

    Performance performance = Performance.create(request);

    Long savedId = performanceRepository.save(performance).getId();

    return new PerformanceCreateResponse(savedId);
  }
}
