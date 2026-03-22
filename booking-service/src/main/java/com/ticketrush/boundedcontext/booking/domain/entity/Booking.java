package com.ticketrush.boundedcontext.booking.domain.entity;

import com.ticketrush.boundedcontext.booking.domain.types.BookingStatus;
import com.ticketrush.global.jpa.entity.AutoIdBaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "booking_id"))
public class Booking extends AutoIdBaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "performance_id", nullable = false)
  private Long performanceId;

  @Column(name = "booking_number", length = 50, nullable = false, unique = true)
  private String bookingNumber;

  @Column(name = "price", nullable = false)
  private Long price;

  @Enumerated(EnumType.STRING)
  @Column(name = "booking_status", length = 20, nullable = false)
  private BookingStatus bookingStatus;

  @Builder
  public Booking(
      String bookingNumber,
      Long userId,
      Long performanceId,
      Long price,
      BookingStatus bookingStatus) {
    this.bookingNumber = bookingNumber;
    this.userId = userId;
    this.performanceId = performanceId;
    this.price = price;
    this.bookingStatus = bookingStatus;
  }
}
