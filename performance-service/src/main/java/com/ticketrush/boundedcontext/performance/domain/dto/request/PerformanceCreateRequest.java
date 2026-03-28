package com.ticketrush.boundedcontext.performance.domain.dto.request;

import com.ticketrush.boundedcontext.performance.domain.types.Genre;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

@Builder
public record PerformanceCreateRequest(
    String title,
    String performer,
    Genre genre,
    String description,
    LocalDate showDate,
    LocalTime showTime,
    Integer durationMinutes,
    Long price,
    Integer totalSeats,
    String address,
    String image3dUrl,
    String imageMainUrl,
    List<String> imageGalleryUrls,
    List<String> facilities) {}
