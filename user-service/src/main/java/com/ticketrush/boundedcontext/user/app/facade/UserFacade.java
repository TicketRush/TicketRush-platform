package com.ticketrush.boundedcontext.user.app.facade;

import com.ticketrush.boundedcontext.user.app.dto.request.SocialCreateRequest;
import com.ticketrush.boundedcontext.user.app.dto.response.SocialCreateResponse;
import com.ticketrush.boundedcontext.user.app.usecase.SocialLoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

  private final SocialLoginUseCase socialLoginUseCase;

  // 소셜 로그인
  public SocialCreateResponse socialLogin(SocialCreateRequest request) {
    return socialLoginUseCase.execute(request);
  }
}
