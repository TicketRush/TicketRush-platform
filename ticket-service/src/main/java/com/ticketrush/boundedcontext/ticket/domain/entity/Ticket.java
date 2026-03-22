package com.ticketrush.boundedcontext.ticket.domain.entity;

import com.ticketrush.boundedcontext.ticket.domain.types.TicketStatus;
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
@Table(name = "ticket")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "ticket_id"))
public class Ticket extends AutoIdBaseEntity {

  @Column(name = "booking_id", nullable = false)
  private Long bookingId;

  @Column(name = "booking_number", length = 50, nullable = false)
  private String bookingNumber;

  @Column(name = "qr_code_url", length = 255, nullable = false)
  private String qrCodeUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "ticket_status", length = 20, nullable = false)
  private TicketStatus ticketStatus;

  @Builder
  public Ticket(Long bookingId, String bookingNumber, String qrCodeUrl, TicketStatus ticketStatus) {
    this.bookingId = bookingId;
    this.bookingNumber = bookingNumber;
    this.qrCodeUrl = qrCodeUrl;
    this.ticketStatus = ticketStatus;
  }
}
