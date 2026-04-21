package com.ticketrush.boundedcontext.auth.app.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

// user-service → auth-service 응답 DTO
public record UserServiceSocialLoginResponse(
    @JsonProperty(value = "user_id") Long userId,
    String name,
    @JsonProperty(value = "is_new_user") boolean isNewUser) {}
