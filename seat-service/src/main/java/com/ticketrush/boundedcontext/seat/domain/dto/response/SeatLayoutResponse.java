package com.ticketrush.boundedcontext.seat.domain.dto.response;

public record SeatLayoutResponse(Long seatId, Long seatLayoutId, String rowNo, Integer colNo) {}
