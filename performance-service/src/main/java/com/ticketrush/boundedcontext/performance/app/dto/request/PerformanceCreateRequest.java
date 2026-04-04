package com.ticketrush.boundedcontext.performance.app.dto.request;

import com.ticketrush.boundedcontext.performance.domain.types.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

@Builder
public record PerformanceCreateRequest(
    @NotBlank(message = "공연명은 필수 입력 항목입니다.") String title,
    @NotBlank(message = "출연진 정보는 필수입니다.") String performer,
    @NotNull(message = "장르를 선택해주세요.") Genre genre,
    String description,
    @NotNull(message = "공연 날짜는 필수입니다.") LocalDate showDate,
    @NotNull(message = "공연 시작 시간은 필수입니다.") LocalTime showTime,
    @NotNull(message = "공연 시간은 필수입니다.") @Positive(message = "총 좌석 수는 1개여야 합니다.")
        Integer durationMinutes,
    @NotNull(message = "가격은 필수입니다.") @Positive(message = "가격은 0보다 커야 합니다.") Long price,
    @NotNull(message = "총 좌석 수는 필수입니다.") @Positive(message = "총 좌석 수는 1개 이상이어야 합니다.")
        Integer totalSeats,
    @NotBlank(message = "공연장 주소는 필수입니다.") String address,
    List<String> facilities) {}
