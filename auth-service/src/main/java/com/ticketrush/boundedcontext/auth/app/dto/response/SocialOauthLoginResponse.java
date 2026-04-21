package com.ticketrush.boundedcontext.auth.app.dto.response;

// 서버 -> 프론트로 반환
public record SocialOauthLoginResponse(Long userId, String name, boolean isNewUser) {}
