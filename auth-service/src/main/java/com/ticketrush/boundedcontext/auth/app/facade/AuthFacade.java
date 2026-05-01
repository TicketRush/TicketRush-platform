package com.ticketrush.boundedcontext.auth.app.facade;

import com.ticketrush.boundedcontext.auth.app.dto.request.SocialOauthLoginRequest;
import com.ticketrush.boundedcontext.auth.app.dto.response.SocialOauthLoginResponse;
import com.ticketrush.boundedcontext.auth.app.usecase.SocialOauthLoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

  private final SocialOauthLoginUseCase socialLoginUseCase;

  public SocialOauthLoginResponse socialLogin(SocialOauthLoginRequest request) {
    return socialLoginUseCase.execute(request);
  }
}
