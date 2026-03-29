package com.ticketrush.boundedcontext.performance.in.api.v1;

import com.ticketrush.boundedcontext.performance.app.facade.PerformanceFacade;
import com.ticketrush.boundedcontext.performance.domain.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.domain.dto.response.PerformanceCreateResponse;
import com.ticketrush.global.dto.response.ApiResponse;
import com.ticketrush.global.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/performance")
@RequiredArgsConstructor
public class PerformanceAdminController {

  private final PerformanceFacade performanceFacade;

  @PostMapping
  public ResponseEntity<ApiResponse<PerformanceCreateResponse>> createPerformance(
      @Valid @RequestBody PerformanceCreateRequest request) {

    PerformanceCreateResponse response = performanceFacade.createPerformance(request);

    return ApiResponse.onSuccess(SuccessStatus.CREATED, response);
  }
}
