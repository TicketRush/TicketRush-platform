package com.ticketrush.boundedcontext.seat.out.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.ticketrush.boundedcontext.seat.app.dto.response.SeatLayoutResponse;
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
  @DisplayName("Seat과 마스터 SeatLayout을 조인하여 해당하는 공연의 좌석 정보(DTO)만 조회한다")
  void findSeatLayoutsByPerformanceId() {
    // given
    Long targetPerformanceId = 1L;
    Long otherPerformanceId = 99L;

    // 1. 공연당 1개의 마스터 SeatLayout 세팅 (1대N 구조 반영)
    SeatLayout targetLayout =
        SeatLayout.builder().performanceId(targetPerformanceId).totalRows(10).maxCols(12).build();
    SeatLayout otherLayout =
        SeatLayout.builder().performanceId(otherPerformanceId).totalRows(5).maxCols(5).build();

    targetLayout = entityManager.persist(targetLayout);
    otherLayout = entityManager.persist(otherLayout);

    // 2. 단일 마스터 레이아웃을 참조하는 여러 개의 Seat 데이터 세팅
    Seat seat1 =
        Seat.builder()
            .seatLayoutId(targetLayout.getId())
            .performanceId(targetPerformanceId)
            .seatNumber("A-1")
            .seatStatus(SeatStatus.AVAILABLE)
            .build();
    Seat seat2 =
        Seat.builder()
            .seatLayoutId(targetLayout.getId())
            .performanceId(targetPerformanceId)
            .seatNumber("A-2")
            .seatStatus(SeatStatus.AVAILABLE)
            .build();
    Seat otherSeat =
        Seat.builder()
            .seatLayoutId(otherLayout.getId())
            .performanceId(otherPerformanceId)
            .seatNumber("A-1")
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
            SeatLayoutResponse::seatNumber)
        .containsExactlyInAnyOrder(
            tuple(seat1.getId(), targetLayout.getId(), "A-1"),
            tuple(seat2.getId(), targetLayout.getId(), "A-2"));
  }
}
