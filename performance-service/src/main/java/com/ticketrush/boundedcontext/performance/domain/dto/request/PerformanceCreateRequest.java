package com.ticketrush.boundedcontext.performance.domain.dto.request;

import com.ticketrush.boundedcontext.performance.domain.types.Genre;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceCreateRequest {

  private String title;
  private String performer;
  private Genre genre;
  private String description;
  private LocalDate showDate;
  private LocalTime showTime;
  private Integer durationMinutes;
  private Long price;
  private Integer totalSeats;
  private String address;
  private String image3dUrl;
  private String imageMainUrl;
  private List<String> imageGalleryUrls;
  private List<String> facilities;
}
