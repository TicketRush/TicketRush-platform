package com.ticketrush;

import static org.assertj.core.api.Assertions.assertThat;

import com.ticketrush.boundedcontext.performance.app.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.app.dto.response.PerformanceCreateResponse;
import com.ticketrush.boundedcontext.performance.app.usecase.PerformanceCreateUseCase;
import com.ticketrush.boundedcontext.performance.domain.types.Genre;
import com.ticketrush.boundedcontext.performance.global.util.S3UploadUtils;
import com.ticketrush.boundedcontext.performance.out.repository.PerformanceRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
@EnableAutoConfiguration(
    exclude = {
      io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration.class,
      io.awspring.cloud.autoconfigure.core.AwsAutoConfiguration.class
    })
@Transactional
class PerformanceCreateTest {

  @MockitoBean private S3Client s3Client;

  @MockitoBean private S3UploadUtils s3UploadUtils;

  @Autowired private PerformanceCreateUseCase performanceCreateUseCase;

  @Autowired private PerformanceRepository performanceRepository;

  @Test
  void createPerformanceTest() {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("콘서트")
            .performer("가수")
            .genre(Genre.MUSICAL)
            .description("설명")
            .showDate(LocalDate.now())
            .showTime(LocalTime.of(19, 0))
            .durationMinutes(120)
            .price(50000L)
            .totalSeats(100)
            .address("서울")
            .build();

    MockMultipartFile mainImage =
        new MockMultipartFile(
            "mainImage", "test.png", "image/png", "test image content".getBytes());
    MockMultipartFile model3d =
        new MockMultipartFile(
            "model3d", "test.glb", "application/octet-stream", "test model content".getBytes());
    List<MultipartFile> gallery = List.of();

    PerformanceCreateResponse response =
        performanceCreateUseCase.execute(request, mainImage, model3d, gallery);

    assertThat(response).isNotNull();
    assertThat(response.performanceId()).isNotNull();

    var savedPerformance =
        performanceRepository
            .findById(response.performanceId())
            .orElseThrow(() -> new AssertionError("저장된 공연을 찾을 수 없습니다."));

    assertThat(savedPerformance.getTitle()).isEqualTo(request.title());
    assertThat(savedPerformance.getPerformer()).isEqualTo(request.performer());
    assertThat(savedPerformance.getGenre()).isEqualTo(request.genre());
    assertThat(savedPerformance.getPrice()).isEqualTo(request.price());
    assertThat(savedPerformance.getTotalSeats()).isEqualTo(request.totalSeats());
  }
}
