package com.ticketrush.boundedcontext.seat.in.api.v1;

import com.ticketrush.boundedcontext.seat.app.facade.SeatFacade;
import com.ticketrush.boundedcontext.seat.domain.dto.response.SeatLayoutResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seat")
@RequiredArgsConstructor
public class SeatController {

  private final SeatFacade seatFacade;

  @GetMapping("/{performanceId}/seat-layouts")
  public ResponseEntity<List<SeatLayoutResponse>> getSeatLayouts(@PathVariable Long performanceId) {
    List<SeatLayoutResponse> response = seatFacade.getPerformanceSeatLayouts(performanceId);
    return ResponseEntity.ok(response);
  }
}