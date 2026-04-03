package com.ticketrush.boundedcontext.booking.app.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookingPendingRequest(
    // TODO: 인증 로직 완료 후 컨트롤러 파라미터(예: @AuthenticationPrincipal)로 대체하고 삭제할 필드
    @NotNull(message = "사용자 ID는 필수입니다.") Long userId,
    @NotNull(message = "공연 ID는 필수입니다.") Long performanceId,
    @NotNull(message = "좌석 ID는 필수입니다.") Long seatId,
    @NotNull(message = "가격은 필수입니다.") @Positive(message = "가격은 0보다 커야 합니다.") Long price) {}
