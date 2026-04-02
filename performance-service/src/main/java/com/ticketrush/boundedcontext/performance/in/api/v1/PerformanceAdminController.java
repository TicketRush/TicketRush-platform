package com.ticketrush.boundedcontext.performance.in.api.v1;

import com.ticketrush.boundedcontext.performance.app.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.app.dto.response.PerformanceCreateResponse;
import com.ticketrush.boundedcontext.performance.app.facade.PerformanceFacade;
import com.ticketrush.global.dto.response.ApiResponse;
import com.ticketrush.global.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  @PostMapping
  public ResponseEntity<ApiResponse<PerformanceCreateResponse>> createPerformance(
      @Valid @RequestBody PerformanceCreateRequest request) {

    PerformanceCreateResponse response = performanceFacade.createPerformance(request);

    return ApiResponse.onSuccess(SuccessStatus.CREATED, response);
  }
}
