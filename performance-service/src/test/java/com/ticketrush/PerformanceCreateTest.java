package com.ticketrush;

import static org.assertj.core.api.Assertions.assertThat;

import com.ticketrush.boundedcontext.performance.app.usecase.PerformanceCreateUseCase;
import com.ticketrush.boundedcontext.performance.domain.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.domain.dto.response.PerformanceCreateResponse;
import com.ticketrush.boundedcontext.performance.domain.types.Genre;
import com.ticketrush.boundedcontext.performance.out.repository.PerformanceRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PerformanceCreateTest {

  @Autowired private PerformanceCreateUseCase performanceCreateUseCase;

  @Autowired private PerformanceRepository performanceRepository;

  @Test
  void createPerformanceTest() {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("콘서트")
            .performer("가수")
            .genre(Genre.MUSICAL)
            .description("설명")
            .showDate(LocalDate.now())
            .showTime(LocalTime.of(19, 0))
            .durationMinutes(120)
            .price(50000L)
            .totalSeats(100)
            .address("서울")
            .build();

    PerformanceCreateResponse response = performanceCreateUseCase.execute(request);

    assertThat(response).isNotNull();
    assertThat(response.performanceId()).isNotNull();

    var savedPerformance =
        performanceRepository
            .findById(response.performanceId())
            .orElseThrow(() -> new AssertionError("저장된 공연을 찾을 수 없습니다."));

    assertThat(savedPerformance.getTitle()).isEqualTo(request.title());
    assertThat(savedPerformance.getPerformer()).isEqualTo(request.performer());
    assertThat(savedPerformance.getGenre()).isEqualTo(request.genre());
    assertThat(savedPerformance.getPrice()).isEqualTo(request.price());
    assertThat(savedPerformance.getTotalSeats()).isEqualTo(request.totalSeats());
  }
}
