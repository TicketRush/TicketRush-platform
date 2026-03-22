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
  private Long performanceId; // 공연 ID (단순 참조)

  @Column(nullable = false, length = 10)
  private String seatNumber; // 예: "A-5"

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SeatStatus seatStatus; // AVAILABLE, HOLDING, SOLD

  @Column(name = "hold_expires_at")
  private LocalDateTime holdExpiresAt; // 5분 임시 선점 만료 시간

  @Builder
  public Seat(
      Long performanceId, String seatNumber, SeatStatus seatStatus, LocalDateTime holdExpiresAt) {
    this.performanceId = performanceId;
    this.seatNumber = seatNumber;
    this.seatStatus = seatStatus;
    this.holdExpiresAt = holdExpiresAt;
  }
}
