package com.ticketrush.boundedcontext.user.app.facade;

import com.ticketrush.boundedcontext.user.app.dto.request.SocialLoginRequest;
import com.ticketrush.boundedcontext.user.app.dto.response.SocialLoginResponse;
import com.ticketrush.boundedcontext.user.app.usecase.SocialLoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

  private final SocialLoginUseCase socialLoginUseCase;

  // 소셜 로그인
  public SocialLoginResponse socialLogin(SocialLoginRequest request) {
    return socialLoginUseCase.execute(request);
  }
}
