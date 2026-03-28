package com.ticketrush.boundedcontext.performance.app.usecase;

import com.ticketrush.boundedcontext.performance.domain.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.domain.entity.Performance;
import com.ticketrush.boundedcontext.performance.domain.types.PerformanceStatus;
import com.ticketrush.boundedcontext.performance.out.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PerformanceCreateUseCase {

  private final PerformanceRepository performanceRepository;

  public Long execute(PerformanceCreateRequest request) {

    Performance performance =
        Performance.builder()
            .title(request.title())
            .performer(request.performer())
            .genre(request.genre())
            .description(request.description())
            .showDate(request.showDate())
            .showTime(request.showTime())
            .durationMinutes(request.durationMinutes())
            .price(request.price())
            .totalSeats(request.totalSeats())
            .address(request.address())
            .performanceStatus(PerformanceStatus.UPCOMING)
            .image3dUrl(request.image3dUrl())
            .imageMainUrl(request.imageMainUrl())
            .imageGalleryUrls(request.imageGalleryUrls())
            .facilities(request.facilities())
            .build();

    return performanceRepository.save(performance).getId();
  }
}
