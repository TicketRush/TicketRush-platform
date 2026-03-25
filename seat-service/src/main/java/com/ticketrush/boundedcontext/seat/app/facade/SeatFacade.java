package com.ticketrush.boundedcontext.seat.app.facade;

import com.ticketrush.boundedcontext.seat.app.usecase.SeatGetSeatLayoutsUseCase;
import com.ticketrush.boundedcontext.seat.domain.dto.response.SeatLayoutResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatFacade {

  private final SeatGetSeatLayoutsUseCase seatGetSeatLayoutsUseCase;

  public List<SeatLayoutResponse> getPerformanceSeatLayouts(Long performanceId) {
    return seatGetSeatLayoutsUseCase.execute(performanceId);
  }
}
