package com.example.gateway.config.filter;

import com.example.auth.AuthRequestToken;
import com.example.auth.AuthUtil;
import com.example.auth.service.AuthService;
import com.example.auth.service.TokenReIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
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


        }
        // 해당 요청자가 알맞은 권한을 갖고 있지 않는다면?
        // 현재 로직은 여과없이 사용자의 정보를 주입하므로 로직에 실패하더라도 정보가 주입된다
        // 그나마 request 객체에 주입하므로 사용자에게 노출될 위협은 없지만
        // 어찌되었든 토큰은 재발급 받는다. 흠 이게 맞나

//        Optional<AuthDataInformation> authDataInformationOptional = authService.getAuthDataInformation(jwtToken.getToken());
//        if (authDataInformationOptional.isPresent()) {
//            AbstractAuthenticationToken authenticationToken = createAuthenticationToken(authDataInformationOptional.get());
//            return chain.filter(exchange)
//                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
//        }

        return chain.filter(exchange);
    }

//    private AbstractAuthenticationToken createAuthenticationToken(AuthDataInformation authDataInformation) {
//        User user = new User(authDataInformation.getDataSignKey(), authDataInformation.getRole());
//        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
//    }
//
//    private String getAccessToken(ServerHttpRequest request) {
//        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (bearerToken != null && !bearerToken.isBlank() && bearerToken.startsWith(JwtUtil.HEADER_TOKEN_PREFIX)) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
}
