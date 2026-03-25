package com.ticketrush.boundedcontext.seat.out.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.ticketrush.boundedcontext.seat.domain.dto.response.SeatLayoutResponse;
import com.ticketrush.boundedcontext.seat.domain.entity.Seat;
import com.ticketrush.boundedcontext.seat.domain.entity.SeatLayout;
import com.ticketrush.boundedcontext.seat.domain.types.SeatStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest
class SeatRepositoryTest {

  @Autowired private SeatRepository seatRepository;

  @Autowired private TestEntityManager entityManager;

  @Test
  @DisplayName("Seat과 SeatLayout을 조인하여 해당하는 공연의 DTO만 조회한다")
  void findSeatLayoutsByPerformanceId() {
    // given
    Long targetPerformanceId = 1L;
    Long otherPerformanceId = 99L;

    // 1. SeatLayout 데이터 세팅
    SeatLayout layout1 =
        SeatLayout.builder().performanceId(targetPerformanceId).rowNo("A").colNo(1).build();
    SeatLayout layout2 =
        SeatLayout.builder().performanceId(targetPerformanceId).rowNo("A").colNo(2).build();
    SeatLayout otherLayout =
        SeatLayout.builder().performanceId(otherPerformanceId).rowNo("B").colNo(1).build();

    layout1 = entityManager.persist(layout1);
    layout2 = entityManager.persist(layout2);
    otherLayout = entityManager.persist(otherLayout);

    // 2. Seat 데이터 세팅
    Seat seat1 =
        Seat.builder()
            .seatLayoutId(layout1.getId())
            .performanceId(targetPerformanceId)
            .seatNumber("A1")
            .seatStatus(SeatStatus.AVAILABLE)
            .build();
    Seat seat2 =
        Seat.builder()
            .seatLayoutId(layout2.getId())
            .performanceId(targetPerformanceId)
            .seatNumber("A2")
            .seatStatus(SeatStatus.AVAILABLE)
            .build();
    Seat otherSeat =
        Seat.builder()
            .seatLayoutId(otherLayout.getId())
            .performanceId(otherPerformanceId)
            .seatNumber("B1")
            .seatStatus(SeatStatus.AVAILABLE)
            .build();

    entityManager.persist(seat1);
    entityManager.persist(seat2);
    entityManager.persist(otherSeat);

    // DB에 쿼리 반영 후 1차 캐시 초기화
    entityManager.flush();
    entityManager.clear();

    // when
    List<SeatLayoutResponse> result =
        seatRepository.findSeatLayoutsByPerformanceId(targetPerformanceId);

    // then
    assertThat(result)
        .hasSize(2)
        .extracting(
            SeatLayoutResponse::seatId,
            SeatLayoutResponse::seatLayoutId,
            SeatLayoutResponse::rowNo,
            SeatLayoutResponse::colNo)
        .containsExactlyInAnyOrder(
            tuple(seat1.getId(), layout1.getId(), "A", 1),
            tuple(seat2.getId(), layout2.getId(), "A", 2));
  }
}
