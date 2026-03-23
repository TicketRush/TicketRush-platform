package com.ticketrush.boundedcontext.performance.domain.entity;

import com.ticketrush.boundedcontext.performance.domain.enums.Genre;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.*;

@Entity
@Table(name = "performance")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Performance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(length = 200)
  private String subTitle;

  @Enumerated(EnumType.STRING) // Enum을 DB에 문자열(MUSICAL 등)로 저장
  @Column(nullable = false)
  private Genre genre;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private Long price;

  @Column(nullable = false)
  private Integer totalSeats;

  private String address;

  private LocalDate showDate;

  private LocalTime showTime;

  private Integer durationMinutes;

  @Column(columnDefinition = "json")
  private String imageGalleryUrls;

  @Column(columnDefinition = "json")
  private String facilities;
}