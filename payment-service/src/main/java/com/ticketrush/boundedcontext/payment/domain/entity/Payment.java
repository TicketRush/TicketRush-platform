package com.ticketrush.boundedcontext.payment.domain.entity;

import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import com.ticketrush.boundedcontext.payment.domain.types.PaymentProvider;
import com.ticketrush.boundedcontext.payment.domain.types.PaymentStatus;
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
@Table(name = "payment") // 테이블명은 소문자 규칙 준수
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 접근 제어 [cite: 97]
@AttributeOverride(name = "id", column = @Column(name = "payment_id"))
public class Payment extends AutoIdBaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "booking_id", nullable = false)
  private Booking booking;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentProvider provider; // 결제 수단 (예: KAKAO, NAVER)

  @Column(nullable = false)
  private Long amount; // 결제 금액

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status; // 결제 상태 (PENDING, COMPLETED 등) [cite: 90]

  private LocalDateTime paidAt; // 결제 완료 시점

  @Builder
  private Payment(
      Booking booking,
      PaymentProvider provider,
      Long amount,
      PaymentStatus status,
      LocalDateTime paidAt) {
    this.booking = booking;
    this.provider = provider;
    this.amount = amount;
    this.status = status;
    this.paidAt = paidAt;
  }
}
