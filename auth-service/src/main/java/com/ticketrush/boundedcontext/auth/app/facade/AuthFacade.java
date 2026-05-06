package com.ticketrush.boundedcontext.auth.app.facade;

import com.ticketrush.boundedcontext.auth.app.dto.request.SocialOauthLoginRequest;
import com.ticketrush.boundedcontext.auth.app.dto.response.OauthLoginResponse;
import com.ticketrush.boundedcontext.auth.app.dto.response.TokenReissueResponse;
import com.ticketrush.boundedcontext.auth.app.support.ProviderParser;
import com.ticketrush.boundedcontext.auth.app.usecase.OauthLoginUrlUseCase;
import com.ticketrush.boundedcontext.auth.app.usecase.SocialLogoutUseCase;
import com.ticketrush.boundedcontext.auth.app.usecase.SocialOauthLoginUseCase;
import com.ticketrush.boundedcontext.auth.app.usecase.TokenReissueUseCase;
import com.ticketrush.boundedcontext.auth.domain.types.SocialProvider;
import com.ticketrush.global.exception.BusinessException;
import com.ticketrush.global.security.JwtTokenProvider;
import com.ticketrush.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFacade {

  private final SocialOauthLoginUseCase socialOauthLoginUseCase;
  private final OauthLoginUrlUseCase oauthLoginUrlUseCase;
  private final TokenReissueUseCase tokenReissueUseCase;
  private final SocialLogoutUseCase socialLogoutUseCase;
  private final JwtTokenProvider jwtTokenProvider;
  private final ProviderParser providerParser;

  // OAuth 로그인 URL 생성
  public String getOAuthLoginUrl(String provider, String redirectUri) {
    SocialProvider socialProvider = providerParser.parse(provider);
    return oauthLoginUrlUseCase.generateOAuthUrl(socialProvider, redirectUri);
  }

  // 소셜 로그인 + JWT 발급
  public OauthLoginResponse socialLogin(SocialOauthLoginRequest request) {
    return socialOauthLoginUseCase.execute(request);
  }

  // JWT 재발급
  public TokenReissueResponse reissue(String refreshToken) {
    return tokenReissueUseCase.execute(refreshToken);
  }

  // 로그아웃 (요청 전처리 과정이기 때문에 facade에서 검증합니다. UseCase는 “이미 정제된 값”을 가지고 비즈니스 로직만 수행해야 합니다.)
  public void logout(String bearerToken) {

    // 1. null 또는 형식 검증
    if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
      throw new BusinessException(ErrorStatus.UNAUTHORIZED);
    }

    // 2. "Bearer " 이후 토큰 추출
    String accessToken = bearerToken.substring(7);

    // 3. 빈 문자열 검증 (혹시 모를 케이스)
    if (accessToken.isBlank()) {
      throw new BusinessException(ErrorStatus.UNAUTHORIZED);
    }

    // 4. userId 추출 및 로그아웃 처리
    Long userId = jwtTokenProvider.getUserId(accessToken);
    socialLogoutUseCase.execute(userId, accessToken);
  }
}
