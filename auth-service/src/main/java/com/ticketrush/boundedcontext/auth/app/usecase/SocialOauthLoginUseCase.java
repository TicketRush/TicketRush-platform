package com.ticketrush.boundedcontext.auth.app.usecase;

import com.ticketrush.boundedcontext.auth.app.dto.internal.SocialUserInfo;
import com.ticketrush.boundedcontext.auth.app.dto.internal.UserServiceSocialLoginRequest;
import com.ticketrush.boundedcontext.auth.app.dto.internal.UserServiceSocialLoginResponse;
import com.ticketrush.boundedcontext.auth.app.dto.request.SocialOauthLoginRequest;
import com.ticketrush.boundedcontext.auth.app.dto.response.SocialOauthLoginResponse;
import com.ticketrush.boundedcontext.auth.out.UserServiceClient;
import com.ticketrush.boundedcontext.auth.out.oauth.SocialOauthService;
import com.ticketrush.boundedcontext.auth.out.oauth.SocialOauthServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
/*
1. provider에 맞는 OAuth 서비스 선택
2. 인가 코드로 소셜 사용자 정보 조회
3. user-service에 회원 식별/생성 요청
4. 최종 응답 반환
 */
public class SocialOauthLoginUseCase {

  private final SocialOauthServiceFactory socialOauthServiceFactory;
  private final UserServiceClient userServiceClient;

  public SocialOauthLoginResponse execute(SocialOauthLoginRequest request) {
    SocialOauthService oauthService = socialOauthServiceFactory.getService(request.provider());
    SocialUserInfo socialUserInfo = oauthService.getUserInfo(request.code());

    UserServiceSocialLoginResponse userResponse =
        userServiceClient.socialLogin(
            new UserServiceSocialLoginRequest(
                socialUserInfo.socialId(),
                socialUserInfo.socialProvider().name(),
                socialUserInfo.name()));

    return new SocialOauthLoginResponse(
        userResponse.userId(), userResponse.name(), userResponse.isNewUser());
  }
}
