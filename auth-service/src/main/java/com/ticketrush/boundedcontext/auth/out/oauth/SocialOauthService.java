package com.ticketrush.boundedcontext.auth.out.oauth;

import com.ticketrush.boundedcontext.auth.app.dto.SocialUserInfo;
import com.ticketrush.boundedcontext.auth.domain.types.SocialProvider;

public interface SocialOauthService {

  SocialProvider getProvider();

  SocialUserInfo getUserInfo(String code);
}
