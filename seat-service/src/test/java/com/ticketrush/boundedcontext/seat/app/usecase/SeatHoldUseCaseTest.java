package com.ticketrush.boundedcontext.seat.app.usecase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ticketrush.boundedcontext.seat.domain.entity.Seat;
import com.ticketrush.boundedcontext.seat.out.repository.SeatRepository;
import com.ticketrush.global.exception.BusinessException;
import com.ticketrush.global.status.ErrorStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeatHoldUseCaseTest {

  @InjectMocks private SeatHoldUseCase seatHoldUseCase;

  @Mock private SeatRepository seatRepository;

  @Test
  @DisplayName("성공: 좌석이 존재하면 hold 상태로 변경한다")
  void execute_success() {
    // given
    Long seatId = 1L;
    LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);
    Seat mockSeat = mock(Seat.class);

    given(seatRepository.findById(seatId)).willReturn(Optional.of(mockSeat));

    // when
    seatHoldUseCase.execute(seatId, expiredAt);

    // then
    verify(mockSeat).hold(expiredAt);
  }

  @Test
  @DisplayName("실패: 좌석이 존재하지 않으면 BusinessException(SEAT_NOT_FOUND)이 발생한다")
  void execute_fail_seat_not_found() {
    // given
    Long seatId = 1L;
    LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

    given(seatRepository.findById(seatId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> seatHoldUseCase.execute(seatId, expiredAt))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorStatus.SEAT_NOT_FOUND.getMessage())
        .extracting("errorStatus")
        .isEqualTo(ErrorStatus.SEAT_NOT_FOUND);
  }
}
