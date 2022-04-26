package com.example.gateway.config.filter;

import com.example.auth.service.AuthService;
import com.example.token.jwt.domain.JwtToken;
import com.example.token.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static com.example.token.jwt.util.JwtUtil.*;

@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    private final AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String accessToken = getAccessToken(request);

        List<HttpCookie> httpCookies = request.getCookies().get(REFRESH_TOKEN_SYNTAX);
        if (httpCookies != null && httpCookies.stream().findFirst().isPresent()) {
            String refreshToken = httpCookies.stream().findFirst().get().getValue();
            Optional<JwtToken> jwtTokenOptional = authService.reIssueToken(accessToken, refreshToken);

            if (jwtTokenOptional.isPresent()) {
                JwtToken jwtToken = jwtTokenOptional.get();
                if (!jwtToken.getToken().equals(accessToken) && !jwtToken.getRefresh().equals(refreshToken)) {
                    JwtUtil.reactiveInjectAuthorization(jwtToken.getToken(), jwtToken.getRefresh(), exchange.getResponse());
                }

                Optional<AuthDataInformation> authDataInformationOptional = authService.getAuthDataInformation(jwtToken.getToken());
                if (authDataInformationOptional.isPresent()) {
                    AbstractAuthenticationToken authenticationToken = createAuthenticationToken(authDataInformationOptional.get());
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
                }
            }
        }

        return chain.filter(exchange);
    }

    private AbstractAuthenticationToken createAuthenticationToken(AuthDataInformation authDataInformation) {
        User user = new User(authDataInformation.getDataSignKey(), authDataInformation.getRole());
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    private String getAccessToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (bearerToken != null && !bearerToken.isBlank() && bearerToken.startsWith(JwtAuthUtil.HEADER_TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
