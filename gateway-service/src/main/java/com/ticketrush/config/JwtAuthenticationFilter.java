package com.ticketrush.config;

import com.ticketrush.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    log.info("🔥 JwtAuthenticationFilter 실행");

    String token = resolveToken(exchange);

    // 토큰 없으면 그냥 통과 (로그인 API 등)
    if (token == null) {
      log.info("🔥 토큰 없음");
      return chain.filter(exchange);
    }

    // JWT 검증
    if (!jwtTokenProvider.validateToken(token)) {
      log.info("🔥 토큰 검증 실패");

      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    // AccessToken 여부 검증
    String type = jwtTokenProvider.getType(token);

    if (!"access".equals(type)) {
      log.info("🔥 AccessToken 아님");

      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    log.info("🔥 토큰 검증 성공");

    // 토큰에서 사용자 정보 추출
    Long userId = jwtTokenProvider.getUserId(token);
    String role = jwtTokenProvider.getRole(token);

    log.info("🔥 userId = {}", userId);
    log.info("🔥 role = {}", role);

    // 내부 서비스로 사용자 정보 전달
    ServerHttpRequest request =
        exchange
            .getRequest()
            .mutate()
            .header("X-User-Id", String.valueOf(userId))
            .header("X-User-Role", role)
            .build();

    return chain.filter(exchange.mutate().request(request).build());
  }

  private String resolveToken(ServerWebExchange exchange) {

    String bearer = exchange.getRequest().getHeaders().getFirst("Authorization");

    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }

    return null;
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
