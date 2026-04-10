package com.ticketrush.boundedcontext.auth.app.dto.internal;

// auth → user-service 요청 DTO
public record UserServiceSocialLoginRequest(String socialId, String socialProvider, String name) {}
