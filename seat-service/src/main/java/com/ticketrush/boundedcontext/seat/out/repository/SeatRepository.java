package com.ticketrush.boundedcontext.seat.out.repository;

import com.ticketrush.boundedcontext.seat.app.dto.response.SeatLayoutResponse;
import com.ticketrush.boundedcontext.seat.domain.entity.Seat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatRepository extends JpaRepository<Seat, Long> {

  @Query(
      "SELECT new com.ticketrush.boundedcontext.seat.domain.dto.response.SeatLayoutResponse("
          + "s.id, sl.id, sl.rowNo, sl.colNo) "
          + "FROM Seat s "
          + "JOIN SeatLayout sl ON s.seatLayoutId = sl.id "
          + "WHERE s.performanceId = :performanceId")
  List<SeatLayoutResponse> findSeatLayoutsByPerformanceId(
      @Param("performanceId") Long performanceId);
}
