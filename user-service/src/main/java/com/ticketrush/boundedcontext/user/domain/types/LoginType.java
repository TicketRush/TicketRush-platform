package com.ticketrush.boundedcontext.user.domain.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {
  LOCAL("자체 회원"),
  SOCIAL("소셜 로그인 회원");

  private final String description;
}
