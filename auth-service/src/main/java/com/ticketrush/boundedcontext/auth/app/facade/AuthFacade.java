package com.ticketrush.boundedcontext.auth.app.facade;

import com.ticketrush.boundedcontext.auth.app.dto.request.SocialOauthLoginRequest;
import com.ticketrush.boundedcontext.auth.app.dto.response.OauthLoginResponse;
import com.ticketrush.boundedcontext.auth.app.dto.response.SocialOauthLoginResponse;
import com.ticketrush.boundedcontext.auth.app.dto.response.TokenReissueResponse;
import com.ticketrush.boundedcontext.auth.app.usecase.OauthLoginUrlUseCase;
import com.ticketrush.boundedcontext.auth.app.usecase.SocialOauthLoginUseCase;
import com.ticketrush.boundedcontext.auth.domain.types.SocialProvider;
import com.ticketrush.boundedcontext.auth.out.RedisService;
import com.ticketrush.global.exception.BusinessException;
import com.ticketrush.global.security.JwtTokenProvider;
import com.ticketrush.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

  private final SocialOauthLoginUseCase socialLoginUseCase;
  private final OauthLoginUrlUseCase oauthLoginUrlUseCase;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisService redisService;

  // OAuth 로그인 URL 생성
  public String getOAuthLoginUrl(String provider, String redirectUri) {
    SocialProvider socialProvider = parseProvider(provider);
    return oauthLoginUrlUseCase.generateOAuthUrl(socialProvider, redirectUri);
  }

  // 소셜 로그인 + JWT 발급
  public OauthLoginResponse socialLogin(SocialOauthLoginRequest request) {

    // 1. 사용자 식별 (회원 조회 or 생성)
    SocialOauthLoginResponse userResponse = socialLoginUseCase.execute(request);

    Long userId = userResponse.userId();
    String name = userResponse.name();
    Boolean isNewUser = userResponse.isNewUser();

    // 2. JWT 생성
    String accessToken = jwtTokenProvider.createAccessToken(userId, "USER");
    String refreshToken = jwtTokenProvider.createRefreshToken(userId);

    // 3. Redis 저장 (핵심)
    redisService.saveRefreshToken(
        userId, refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

    // 4. 응답
    return new OauthLoginResponse(
        userId,
        name,
        isNewUser,
        accessToken,
        refreshToken,
        jwtTokenProvider.getAccessTokenExpiration(),
        jwtTokenProvider.getRefreshTokenExpiration());
  }

  // JWT 재발급
  public TokenReissueResponse reissue(String refreshToken) {

    // 1. refresh token 검증
    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new BusinessException(ErrorStatus.AUTH_INVALID_REFRESH_TOKEN);
    }

    // 2. userId 추출
    Long userId = jwtTokenProvider.getUserId(refreshToken);

    // 3. Redis 검증 (진짜 핵심)
    if (!redisService.isValidRefreshToken(userId, refreshToken)) {
      throw new BusinessException(ErrorStatus.AUTH_INVALID_REFRESH_TOKEN);
    }

    // 4. 새 토큰 발급
    String newAccessToken = jwtTokenProvider.createAccessToken(userId, "USER");
    String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

    // 5. Redis 갱신
    redisService.saveRefreshToken(
        userId, newRefreshToken, jwtTokenProvider.getRefreshTokenExpiration());

    return new TokenReissueResponse(
        newAccessToken,
        newRefreshToken,
        jwtTokenProvider.getAccessTokenExpiration(),
        jwtTokenProvider.getRefreshTokenExpiration());
  }

  // 변환 메서드 (string -> enum)
  private SocialProvider parseProvider(String provider) {
    try {
      return SocialProvider.valueOf(provider.toUpperCase());
    } catch (Exception e) {
      throw new BusinessException(ErrorStatus.AUTH_PROVIDER_NOT_SUPPORT);
    }
  }
}
