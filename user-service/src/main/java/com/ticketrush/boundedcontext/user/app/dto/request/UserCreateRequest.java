package com.ticketrush.boundedcontext.user.app.dto.request;

import com.ticketrush.boundedcontext.user.domain.types.SocialProvider;

public record UserCreateRequest(
    String socialId, // 사용자 고유 ID (처음 로그인했기 때문에 socialId)
    SocialProvider socialProvider, // 카카오, 네이버, 구글
    String name, // 닉네임
    String profileImage // 프로필 이미지 URL
) {}
