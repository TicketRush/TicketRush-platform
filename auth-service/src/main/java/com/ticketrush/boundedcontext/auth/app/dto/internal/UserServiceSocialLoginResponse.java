package com.ticketrush.boundedcontext.auth.app.dto.internal;

// user-service → auth-service 응답 DTO
public record UserServiceSocialLoginResponse(Long userId, String name, Boolean isNewUser) {}
