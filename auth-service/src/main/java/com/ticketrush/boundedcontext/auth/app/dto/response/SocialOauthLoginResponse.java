package com.ticketrush.boundedcontext.auth.app.dto.response;

// 서버 -> 프론트로 반환
public record SocialOauthLoginResponse(
    Long userId, String name, Boolean isNewUser) {} // true = 회원가입 직후, false = 기존 유저
