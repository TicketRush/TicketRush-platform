package com.ticketrush.boundedcontext.performance.in.api.v1;

import com.ticketrush.boundedcontext.performance.app.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.app.dto.response.PerformanceCreateResponse;
import com.ticketrush.boundedcontext.performance.app.facade.PerformanceFacade;
import com.ticketrush.global.dto.response.ApiResponse;
import com.ticketrush.global.status.SuccessStatus;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** [TODO] 스웨거 설정 PR 머지 후 어노테이션 활성화 예정 @Tag(name = "Performance", description = "공연 도메인 관리 API") */
@RestController
@RequestMapping("/api/v1/performance")
@RequiredArgsConstructor
public class PerformanceAdminController {

  private final PerformanceFacade performanceFacade;

  /**
   * [TODO] 스웨거 설정 PR 머지 후 어노테이션 활성화 예정 @Operation(summary = "공연 전시 등록", description = "새로운 공연 정보를
   * 등록합니다.")
   */
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<PerformanceCreateResponse>> createPerformance(
      @RequestPart("request") @Valid PerformanceCreateRequest request,
      @RequestPart("mainImage") MultipartFile mainImage,
      @RequestPart("model3d") MultipartFile model3d,
      @RequestPart(value = "gallery", required = false) List<MultipartFile> gallery) {

    // Facade에서 파일 업로드와 정보 저장을 함께 처리하도록 위임
    PerformanceCreateResponse response =
        performanceFacade.createPerformance(request, mainImage, model3d, gallery);
    return ApiResponse.onSuccess(SuccessStatus.CREATED, response);
  }
}
