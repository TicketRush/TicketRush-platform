package com.ticketrush.boundedcontext.performance.domain.entity;

import com.ticketrush.boundedcontext.performance.domain.types.Genre;
import com.ticketrush.boundedcontext.performance.domain.types.PerformanceStatus;
import com.ticketrush.global.jpa.entity.AutoIdBaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "performance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@AttributeOverride(name = "id", column = @Column(name = "performance_id"))
public class Performance extends AutoIdBaseEntity {

  @Column(nullable = false, length = 200)
  private String title;

  @Column(length = 200)
  private String performer;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
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

  @Column(length = 255)
  private String address;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PerformanceStatus performanceStatus;

  private String image3dUrl;

  private String imageMainUrl;

  @ElementCollection
  @CollectionTable(name = "performance_images", joinColumns = @JoinColumn(name = "performance_id"))
  @Column(name = "image_url")
  private List<String> imageGalleryUrls;

  @ElementCollection
  @CollectionTable(
      name = "performance_facilities",
      joinColumns = @JoinColumn(name = "performance_id"))
  @Column(name = "facility_name")
  private List<String> facilities;
}
