package com.ticketrush.boundedcontext.seat.app.usecase;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.ticketrush.boundedcontext.seat.domain.entity.Seat;
import com.ticketrush.boundedcontext.seat.out.repository.SeatRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeatReleaseSingleUseCaseTest {

  @Mock private SeatRepository seatRepository;

  @InjectMocks private SeatReleaseSingleUseCase seatReleaseSingleUseCase;

  @Test
  @DisplayName("존재하는 좌석의 만료 이벤트 수신 시 상태를 AVAILABLE로 롤백한다")
  void execute_WhenSeatExists_ReleasesHold() {
    // given
    Long seatId = 1L;
    Seat mockSeat = mock(Seat.class);
    given(seatRepository.findById(seatId)).willReturn(Optional.of(mockSeat));

    // when
    seatReleaseSingleUseCase.execute(seatId);

    // then
    verify(seatRepository).findById(seatId);
    verify(mockSeat).releaseHold(); // 엔티티의 상태 변경 메서드가 호출되었는지 검증
  }

  @Test
  @DisplayName("존재하지 않는 좌석의 만료 이벤트 수신 시 예외를 던지지 않고 경고 로그만 남긴다")
  void execute_WhenSeatDoesNotExist_DoesNotThrow() {
    // given
    Long seatId = 999L;
    given(seatRepository.findById(seatId)).willReturn(Optional.empty());

    // when
    seatReleaseSingleUseCase.execute(seatId);

    // then
    verify(seatRepository).findById(seatId);
    verify(mock(Seat.class), never()).releaseHold();
  }
}
