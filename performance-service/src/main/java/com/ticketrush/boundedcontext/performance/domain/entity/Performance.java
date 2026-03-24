package com.ticketrush.boundedcontext.performance.domain.entity;

import com.ticketrush.boundedcontext.performance.domain.enums.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "performance")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Performance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "performance_id") // ERD PK 명칭 매핑
  private Long performanceId;

  @Column(nullable = false, length = 200) // ERD 기준 길이 수정
  private String title;

  @Column(length = 200)
  private String performer; // 누락 필드 추가

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private Genre genre;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private LocalDate showDate;

  @Column(nullable = false)
  private LocalTime showTime;

  @Column(nullable = false)
  private Integer durationMinutes;

  @Column(nullable = false)
  private Long price;

  @Column(nullable = false)
  private Integer totalSeats;

  private String address;

  private String status;

  private String image3dUrl;
  private String imageMainUrl;

  @Column(columnDefinition = "json")
  private String imageGalleryUrls;

  @Column(columnDefinition = "json")
  private String facilities;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
}