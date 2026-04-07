package com.ticketrush.boundedcontext.user.app.usecase;

import com.ticketrush.boundedcontext.user.app.dto.request.UserCreateRequest;
import com.ticketrush.boundedcontext.user.app.dto.response.UserCreateResponse;
import com.ticketrush.boundedcontext.user.domain.entity.SocialAccount;
import com.ticketrush.boundedcontext.user.domain.entity.User;
import com.ticketrush.boundedcontext.user.domain.types.UserRole;
import com.ticketrush.boundedcontext.user.out.SocialAccountRepository;
import com.ticketrush.boundedcontext.user.out.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SocialLoginUseCase {

  private final SocialAccountRepository socialAccountRepository;
  private final UserRepository userRepository;

  @Transactional
  public UserCreateResponse execute(UserCreateRequest request) {

    // 1. 소셜 계정 조회
    Optional<SocialAccount> account =
        socialAccountRepository.findBySocialProviderAndProviderUserId(
            request.socialProvider(), request.socialId());

    // 2. 기존 유저라면 로그인 처리
    if (account.isPresent()) {
      User user = account.get().getUser();

      return new UserCreateResponse(user.getId(), user.getName(), user.getProfileImage(), false);
    }

    // 3. 신규 유저 생성
    User newUser =
        User.builder()
            .name(request.name())
            .profileImage(request.profileImage())
            .userRole(UserRole.MEMBER)
            .build();

    userRepository.save(newUser);

    // 4. 소셜 계정 생성
    SocialAccount socialAccount =
        SocialAccount.builder()
            .user(newUser)
            .socialProvider(request.socialProvider())
            .providerUserId(request.socialId())
            .build();

    socialAccountRepository.save(socialAccount);

    return new UserCreateResponse(
        newUser.getId(), newUser.getName(), newUser.getProfileImage(), true);
  }
}
