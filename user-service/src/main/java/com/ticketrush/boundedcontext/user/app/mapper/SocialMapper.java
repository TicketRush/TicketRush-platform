package com.ticketrush.boundedcontext.user.app.mapper;

import com.ticketrush.boundedcontext.user.app.dto.request.SocialCreateRequest;
import com.ticketrush.boundedcontext.user.domain.entity.SocialAccount;
import com.ticketrush.boundedcontext.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SocialMapper {

  // SocialCreateRequest → User Entity 변환
  @Mapping(source = "name", target = "name")
  @Mapping(target = "userRole", constant = "MEMBER")
  User toUser(SocialCreateRequest request);

  // SocialCreateRequest → SocialAccount Entity 변환
  @Mapping(target = "user", ignore = true) // 서비스에서 연결
  @Mapping(source = "socialId", target = "providerUserId")
  @Mapping(source = "socialProvider", target = "socialProvider")
  SocialAccount toSocialAccount(SocialCreateRequest request);
}
