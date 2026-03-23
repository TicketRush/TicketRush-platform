package com.ticketrush.boundedcontext.seat.domain.entity;

import com.ticketrush.boundedcontext.seat.domain.types.SeatStatus;
import com.ticketrush.global.jpa.entity.AutoIdBaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "seat_id"))
public class Seat extends AutoIdBaseEntity {

  @Column(nullable = false)
  private Long seatLayoutId;

  @Column(nullable = false)
  private Long performanceId;

  @Column(nullable = false, length = 10)
  private String seatNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SeatStatus seatStatus;

  @Column(name = "hold_expired_at")
  private LocalDateTime holdExpiredAt;

  @Builder
  public Seat(
      Long seatLayoutId, Long performanceId, String seatNumber, SeatStatus seatStatus, LocalDateTime holdExpiredAt) {
    this.seatLayoutId = seatLayoutId;
    this.performanceId = performanceId;
    this.seatNumber = seatNumber;
    this.seatStatus = seatStatus;
    this.holdExpiredAt = holdExpiredAt;
  }
}