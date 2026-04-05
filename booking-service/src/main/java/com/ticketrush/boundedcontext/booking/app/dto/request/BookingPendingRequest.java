package com.ticketrush.boundedcontext.booking.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "예약 대기 생성 요청 DTO")
public record BookingPendingRequest(
    // TODO: 인증 로직 완료 후 컨트롤러 파라미터(예: @AuthenticationPrincipal)로 대체하고 삭제할 필드
    @Schema(description = "사용자 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Long userId,
    @Schema(description = "공연 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Long performanceId,
    @Schema(description = "좌석 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Long seatId) {}
