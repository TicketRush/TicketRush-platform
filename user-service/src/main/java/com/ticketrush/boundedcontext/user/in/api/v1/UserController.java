package com.ticketrush.boundedcontext.user.in.api.v1;

import com.ticketrush.boundedcontext.user.app.dto.request.SocialCreateRequest;
import com.ticketrush.boundedcontext.user.app.dto.response.SocialCreateResponse;
import com.ticketrush.boundedcontext.user.app.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "회원 로그인 API")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @Operation(summary = "소셜로그인 회원 등록", description = "소셜로그인을 통해 회원을 등록합니다.")
  @PostMapping("/social-login")
  public SocialCreateResponse socialLogin(@RequestBody SocialCreateRequest request) {
    return userFacade.socialLogin(request);
  }
}
