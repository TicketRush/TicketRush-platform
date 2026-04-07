package com.ticketrush.boundedcontext.user.app.usecase;

import com.ticketrush.boundedcontext.user.app.dto.request.SocialCreateRequest;
import com.ticketrush.boundedcontext.user.app.dto.response.SocialCreateResponse;
import com.ticketrush.boundedcontext.user.app.mapper.SocialMapper;
import com.ticketrush.boundedcontext.user.domain.entity.SocialAccount;
import com.ticketrush.boundedcontext.user.domain.entity.User;
import com.ticketrush.boundedcontext.user.out.SocialAccountRepository;
import com.ticketrush.boundedcontext.user.out.UserRepository;
import com.ticketrush.global.exception.BusinessException;
import com.ticketrush.global.status.ErrorStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SocialLoginUseCase {

  private final SocialAccountRepository socialAccountRepository;
  private final UserRepository userRepository;
  private final SocialMapper socialMapper;

  @Transactional
  public SocialCreateResponse execute(SocialCreateRequest request) {

    validation(request);

    // 1. 소셜 계정 조회
    Optional<SocialAccount> account =
        socialAccountRepository.findBySocialProviderAndProviderUserId(
            request.socialProvider(), request.socialId());

    // 2. 기존 유저라면 로그인 처리
    if (account.isPresent()) {
      User user = account.get().getUser();

      return new SocialCreateResponse(user.getId(), user.getName(), false);
    }

    // 3. 신규 유저 생성
    User newUser = socialMapper.toUser(request);
    userRepository.save(newUser);

    // 4. 소셜 계정 생성
    SocialAccount socialAccount = socialMapper.toSocialAccount(request); // newRequest =
    socialAccount.setUser(newUser);

    socialAccountRepository.save(socialAccount);

    return new SocialCreateResponse(
        newUser.getId(), newUser.getName(), true // isNewUser는 서비스에서 결정
        );
  }

  private void validation(SocialCreateRequest request) {
    if (request.socialProvider() == null) {
      throw new BusinessException(ErrorStatus.USER_SOCIAL_PROVIDER_REQUIRED);
    }
  }
}
