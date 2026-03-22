package com.ticketrush.boundedcontext.ticket.domain.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicketStatus {
  VALID("유효함"), // 예매가 확정되어 발급된 정상 티켓
  USED("사용 완료"), // 공연장 입구에서 QR 스캔을 통해 입장이 완료된 상태
  CANCELED("취소됨"); // 사용자가 예매를 취소하여 무효화된 티켓

  private final String description;
}
