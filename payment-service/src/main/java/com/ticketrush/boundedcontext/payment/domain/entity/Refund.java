package com.ticketrush.boundedcontext.payment.domain.entity;

import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import com.ticketrush.boundedcontext.payment.domain.types.RefundStatus;
import com.ticketrush.global.jpa.entity.AutoIdBaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refund") // 테이블명 소문자 규칙 [cite: 65]
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "refund_id"))
public class Refund extends AutoIdBaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "booking_id", nullable = false)
  private Booking booking;

  @Column(nullable = false)
  private Long price; // 환불 금액

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 20, nullable = true)
  private RefundStatus status; // 환불 상태 (PENDING, COMPLETED 등)

  private LocalDateTime confirmedAt; // {동사}edAt 규칙 준수 [cite: 65]

  @Builder
  private Refund(Booking booking, Long price, RefundStatus status, LocalDateTime confirmedAt) {
    this.booking = booking;
    this.price = price;
    this.status = status;
    this.confirmedAt = confirmedAt;
  }
}
