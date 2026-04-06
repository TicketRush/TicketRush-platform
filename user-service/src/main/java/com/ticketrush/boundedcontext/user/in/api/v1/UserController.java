package com.ticketrush.boundedcontext.user.in.api.v1;

import com.ticketrush.boundedcontext.user.app.dto.request.UserCreateRequest;
import com.ticketrush.boundedcontext.user.app.dto.response.UserCreateResponse;
import com.ticketrush.boundedcontext.user.app.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @PostMapping("/social-login")
  public UserCreateResponse socialLogin(@RequestBody UserCreateRequest request) {
    return userFacade.socialLogin(request);
  }
}
