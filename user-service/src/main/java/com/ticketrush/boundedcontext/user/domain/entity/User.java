package com.ticketrush.boundedcontext.user.domain.entity;

import com.ticketrush.boundedcontext.user.domain.types.LoginType;
import com.ticketrush.boundedcontext.user.domain.types.UserRole;
import com.ticketrush.global.jpa.entity.AutoIdBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends AutoIdBaseEntity {

  @Column(nullable = true, length = 50)
  private String name;

  @Column(nullable = true, length = 100)
  private String email;

  @Column(nullable = true, length = 30)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_role", nullable = false)
  private UserRole role;

  @Enumerated(EnumType.STRING)
  @Column(name = "login_type", nullable = false)
  private LoginType type;

  @Builder
  public User(String name, String email, String phone, UserRole role, LoginType type) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.role = role;
    this.type = type;
  }
}
