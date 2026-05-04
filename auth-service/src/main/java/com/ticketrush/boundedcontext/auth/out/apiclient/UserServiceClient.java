package com.ticketrush.boundedcontext.auth.out.apiclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketrush.boundedcontext.auth.app.dto.request.UserServiceSocialLoginRequest;
import com.ticketrush.boundedcontext.auth.app.dto.response.UserServiceSocialLoginResponse;
import com.ticketrush.global.exception.BusinessException;
import com.ticketrush.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

// auth-service가 user-service를 호출하기 위한 전용 클라이언트
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceClient {

  private final RestClient restClient;

  @Value("${service.user-service.base-url}")
  private String userServiceBaseUrl;

  public UserServiceSocialLoginResponse socialLogin(UserServiceSocialLoginRequest request) {

    log.info("🔥 user-service로 보내는 request = {}", request);
    try {
      log.info("🔥 JSON = {}", new ObjectMapper().writeValueAsString(request));
    } catch (JsonProcessingException e) {
      log.error("JSON 변환 실패", e);
    }

    UserServiceSocialLoginResponse response =
        restClient
            .post()
            .uri(userServiceBaseUrl + "/api/v1/user/social-login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                (req, res) -> {
                  log.error("user-service 4xx 에러 발생: status={}", res.getStatusCode());
                  throw new BusinessException(ErrorStatus.AUTH_USER_BAD_REQUEST);
                })
            .onStatus(
                HttpStatusCode::is5xxServerError,
                (req, res) -> {
                  log.error("user-service 5xx 에러 발생: status={}", res.getStatusCode());
                  throw new BusinessException(ErrorStatus.AUTH_USER_SERVER_ERROR);
                })
            .body(UserServiceSocialLoginResponse.class);

    if (response == null) {
      throw new BusinessException(ErrorStatus.AUTH_USER_COMMUNICATION_FAILED);
    }

    return response;
  }
}
