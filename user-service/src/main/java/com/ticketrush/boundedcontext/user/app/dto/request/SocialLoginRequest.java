package com.ticketrush.boundedcontext.user.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {

  @JsonProperty("socialId")
  private String socialId;

  @JsonProperty("socialProvider")
  private String socialProvider;

  @JsonProperty("name")
  private String name;

  @Override
  public String toString() {
    return "SocialLoginRequest{"
        + "socialId='"
        + socialId
        + '\''
        + ", socialProvider='"
        + socialProvider
        + '\''
        + ", name='"
        + name
        + '\''
        + '}';
  }
}
