package com.ticketrush.boundedcontext.user.app.dto.response;

public record UserCreateResponse(
    Long userId,
    String name,
    String profileImage,
    boolean isNewUser // true = 회원가입 직후, false = 기존 유저
) {}

