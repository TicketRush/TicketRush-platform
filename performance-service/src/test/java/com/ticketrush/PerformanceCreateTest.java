package com.ticketrush;

import static org.assertj.core.api.Assertions.assertThat;

import com.ticketrush.boundedcontext.performance.app.usecase.PerformanceCreateUseCase;
import com.ticketrush.boundedcontext.performance.domain.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.domain.types.Genre;
import com.ticketrush.boundedcontext.performance.out.repository.PerformanceRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@Transactional
class PerformanceCreateTest {

  @Autowired private PerformanceCreateUseCase performanceCreateUseCase;

  @Autowired private PerformanceRepository performanceRepository;

  @Test
  void createPerformanceTest() {
    // 1. Given (준비)
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

    Long savedId = performanceCreateUseCase.execute(request);

    assertThat(savedId).isNotNull();
    assertThat(performanceRepository.findById(savedId)).isPresent();

    System.out.println("테스트 성공! 생성된 공연 ID: " + savedId);
  }
}
