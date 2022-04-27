package com.example.gateway.config.filter;

import com.example.auth.AuthRequestToken;
import com.example.auth.AuthUtil;
import com.example.auth.service.AuthDataInformation;
import com.example.auth.service.AuthService;
import com.example.auth.service.TokenReIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    private final AuthService authService;
    private final AuthUtil authUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        AuthRequestToken authRequestToken = authUtil.reactiveExportAuthorization(request);
        if (StringUtils.hasText(authRequestToken.getAccessToken())) {
            if (StringUtils.hasText(authRequestToken.getRefreshToken())) {
                Optional<TokenReIssueResponse> tokenReIssueResponseOpt = authService.reIssueToken(authRequestToken.getAccessToken(), authRequestToken.getRefreshToken());
                if (tokenReIssueResponseOpt.isPresent()) {
                    TokenReIssueResponse tokenReIssueResponse = tokenReIssueResponseOpt.get();
                    if (!tokenReIssueResponse.getAccessToken().equals(authRequestToken.getAccessToken()) &&
                            !tokenReIssueResponse.getRefreshToken().equals(authRequestToken.getRefreshToken())) {
                        authRequestToken = new AuthRequestToken(tokenReIssueResponse.getAccessToken(), tokenReIssueResponse.getRefreshToken());
                        authUtil.reactiveInjectAuthorization(
                                authRequestToken.getAccessToken(),
                                authRequestToken.getRefreshToken(),
                                exchange.getResponse());
                    }
                }
            }

            Optional<AuthDataInformation> authDataInformationOpt = authService.getAuthDataInformation(authRequestToken.getAccessToken());
            if (authDataInformationOpt.isPresent()) {
                AbstractAuthenticationToken authenticationToken = createAuthenticationToken(authDataInformationOpt.get());
                return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
            }
        }
        return chain.filter(exchange);
    }

    private AbstractAuthenticationToken createAuthenticationToken(AuthDataInformation authDataInformation) {
        User user = new User(authDataInformation.getDataSignKey(), authDataInformation.getRoles());
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }
}
