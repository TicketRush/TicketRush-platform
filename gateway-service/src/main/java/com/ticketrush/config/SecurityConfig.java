package com.ticketrush.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties({CorsProperties.class})
@RequiredArgsConstructor
public class SecurityConfig {

  // CORS 허용 출처 목록 설정값을 담고 있는 객체
  private final CorsProperties corsProperties;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        // 명시적으로 직접 생성한 CORS 설정 소스를 Security에 주입
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(ServerHttpSecurity.CsrfSpec::disable) // CSRF 보호 disable
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // 폼 로그인 비활성화 - 스프링 시큐리티 기본 로그인 페이지 끔
        .httpBasic(
            ServerHttpSecurity.HttpBasicSpec
                ::disable) // HTTP Basic 비활성화 - Authorization: Basic 방식 인증 끔
        .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll()) // 모든 인증 허용
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // 허용할 출처
    configuration.setAllowedOrigins(corsProperties.allowedOrigins());

    // 허용할 HTTP 메서드
    configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE 등 모두 허용

    // 허용할 헤더
    configuration.addAllowedHeader("*");

    // 자격 증명 허용 (쿠키나 JWT를 헤더에 담아 보낼 때 필수)
    configuration.setAllowCredentials(true);

    // 브라우저가 응답에서 접근할 수 있는 헤더 (JWT 사용 시 필요할 수 있음)
    configuration.addExposedHeader("Authorization");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용
    return source;
  }
}
