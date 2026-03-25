package com.ticketrush.boundedcontext.seat.app.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ticketrush.boundedcontext.seat.domain.dto.response.SeatLayoutResponse;
import com.ticketrush.boundedcontext.seat.out.repository.SeatRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeatGetSeatLayoutsUseCaseTest {

  @InjectMocks private SeatGetSeatLayoutsUseCase useCase; // 테스트 대상

  @Mock private SeatRepository seatRepository; // 가짜 객체

  @Test
  @DisplayName("공연 ID에 해당하는 정적 좌석 맵 리스트를 반환한다")
  void executeReturnsSeatLayouts() {
    // given
    Long performanceId = 1L;
    List<SeatLayoutResponse> expectedResponses =
        List.of(new SeatLayoutResponse(1L, 101L, "A", 1), new SeatLayoutResponse(2L, 102L, "A", 2));
    given(seatRepository.findSeatLayoutsByPerformanceId(performanceId))
        .willReturn(expectedResponses);

    // when
    List<SeatLayoutResponse> actualResponses = useCase.execute(performanceId);

    // then
    assertThat(actualResponses).hasSize(2);
    assertThat(actualResponses.getFirst().seatId()).isEqualTo(1L);
    assertThat(actualResponses.getFirst().rowNo()).isEqualTo("A");
  }
}
