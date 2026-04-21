package com.ticketrush.boundedcontext.auth.out.oauth;

import com.ticketrush.boundedcontext.auth.domain.types.SocialProvider;
import com.ticketrush.boundedcontext.auth.domain.types.SocialUserInfo;

public interface SocialOauthService {

  SocialProvider getProvider();

  SocialUserInfo getUserInfo(String code);

  String generateOAuthUrl(String redirectUri);
}
