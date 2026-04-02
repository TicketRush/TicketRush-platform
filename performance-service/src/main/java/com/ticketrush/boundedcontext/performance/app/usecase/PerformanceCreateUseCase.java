package com.ticketrush.boundedcontext.performance.app.usecase;

import com.ticketrush.boundedcontext.performance.app.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.app.dto.response.PerformanceCreateResponse;
import com.ticketrush.boundedcontext.performance.domain.entity.Performance;
import com.ticketrush.boundedcontext.performance.global.util.S3UploadUtils;
import com.ticketrush.boundedcontext.performance.out.repository.PerformanceRepository;
import com.ticketrush.global.exception.BusinessException;
import com.ticketrush.global.status.ErrorStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PerformanceCreateUseCase {

  private final S3UploadUtils s3UploadUtils;
  private final PerformanceRepository performanceRepository;

  /** 공연 정보와 파일들을 받아 S3 업로드 후 DB에 저장합니다. */
  @Transactional
  public PerformanceCreateResponse execute(
      PerformanceCreateRequest request,
      MultipartFile mainImage,
      MultipartFile model3d,
      List<MultipartFile> gallery) {

    // 1. 필수 파일 및 갤러리 개수 검증
    validateFiles(mainImage, model3d, gallery);

    // 2. 파일 업로드
    String mainImageUrl = s3UploadUtils.uploadFile(mainImage);
    String model3dUrl = s3UploadUtils.uploadFile(model3d);
    List<String> galleryUrls =
        (gallery != null) ? gallery.stream().map(s3UploadUtils::uploadFile).toList() : List.of();

    // 3. 엔티티 생성
    Performance performance =
        Performance.builder()
            .title(request.title())
            .performer(request.performer())
            .genre(request.genre())
            .description(request.description())
            .showDate(request.showDate())
            .showTime(request.showTime())
            .durationMinutes(request.durationMinutes())
            .price(request.price())
            .totalSeats(request.totalSeats())
            .address(request.address())
            .imageMainUrl(mainImageUrl)
            .image3dUrl(model3dUrl)
            .imageGalleryUrls(galleryUrls)
            .facilities(request.facilities())
            .build();

    Performance savedPerformance = performanceRepository.save(performance);

    return PerformanceCreateResponse.from(savedPerformance);
  }

  private void validateFiles(
      MultipartFile mainImage, MultipartFile model3d, List<MultipartFile> gallery) {
    if (mainImage == null || mainImage.isEmpty() || model3d == null || model3d.isEmpty()) {
      throw new BusinessException(ErrorStatus.BAD_REQUEST);
    }
    if (gallery != null && gallery.size() > 3) {
      throw new BusinessException(ErrorStatus.BAD_REQUEST);
    }
  }
}
