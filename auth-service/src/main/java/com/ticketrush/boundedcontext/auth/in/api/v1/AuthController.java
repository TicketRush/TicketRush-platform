package com.ticketrush.boundedcontext.auth.in.api.v1;

import com.ticketrush.boundedcontext.auth.app.dto.request.SocialOauthLoginRequest;
import com.ticketrush.boundedcontext.auth.app.dto.response.SocialOauthLoginResponse;
import com.ticketrush.boundedcontext.auth.app.facade.AuthFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthFacade authFacade;

  @Operation(summary = "소셜 로그인", description = "소셜 제공자와 인가 코드를 받아 회원 조회 또는 회원 생성을 수행합니다.")
  @PostMapping("/social/login")
  public SocialOauthLoginResponse socialLogin(@RequestBody @Valid SocialOauthLoginRequest request) {
    return authFacade.socialLogin(request);
  }
}
