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
            .title(request.getTitle())
            .performer(request.getPerformer())
            .genre(request.getGenre())
            .description(request.getDescription())
            .showDate(request.getShowDate())
            .showTime(request.getShowTime())
            .durationMinutes(request.getDurationMinutes())
            .price(request.getPrice())
            .totalSeats(request.getTotalSeats())
            .address(request.getAddress())
            .performanceStatus(PerformanceStatus.UPCOMING)
            .image3dUrl(request.getImage3dUrl())
            .imageMainUrl(request.getImageMainUrl())
            .imageGalleryUrls(request.getImageGalleryUrls())
            .facilities(request.getFacilities())
            .build();

    return performanceRepository.save(performance).getId();
  }
}
