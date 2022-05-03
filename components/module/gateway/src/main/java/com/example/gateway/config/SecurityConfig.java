package com.example.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@EnableWebFluxSecurity
public class SecurityConfig {
    @Value("${white-ip:0:0:0:0:0:0:0:1, 127.0.0.1, 192.168.1.1}")
    List<String> whiteIpList;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.authenticationEntryPoint((exchange, ex) -> {
                    return Mono.fromRunnable(() -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    });
                }).accessDeniedHandler((exchange, denied) -> {
                    return Mono.fromRunnable(() -> {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    });
                }))
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/**").access(this::whiteListIp)
                        .anyExchange().authenticated()
                );
        return http.build();
    }

    private Mono<AuthorizationDecision> whiteListIp(Mono<Authentication> authentication, AuthorizationContext context) {
        String ip = Objects.requireNonNull(context.getExchange().getRequest().getRemoteAddress()).getAddress().toString().replace("/", "");

        return authentication.map((a) -> new AuthorizationDecision(a.isAuthenticated()))
                .defaultIfEmpty(new AuthorizationDecision(
                        whiteIpList.contains(ip)
                ));
    }
}
