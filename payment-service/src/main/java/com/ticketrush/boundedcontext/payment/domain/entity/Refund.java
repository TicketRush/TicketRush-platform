package com.ticketrush.boundedcontext.payment.domain.entity;

import com.ticketrush.boundedcontext.payment.domain.types.RefundStatus;
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
@Table(name = "refund")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "refund_id"))
public class Refund extends AutoIdBaseEntity {

  @Column(nullable = false)
  private Long bookingId;

  @Column(nullable = false)
  private Long price; // 환불 금액

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 20)
  private RefundStatus status; // 환불 상태 (PENDING, COMPLETED 등)

  private LocalDateTime confirmedAt;

  @Builder
  private Refund(Long bookingId, Long price, RefundStatus status, LocalDateTime confirmedAt) {
    this.bookingId = bookingId;
    this.price = price;
    this.status = status;
    this.confirmedAt = confirmedAt;
  }
}
