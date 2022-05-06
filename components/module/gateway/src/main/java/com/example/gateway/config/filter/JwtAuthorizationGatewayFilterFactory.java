package com.example.gateway.config.filter;

import com.example.auth.store.service.AuthDataInformation;
import com.example.auth.store.service.AuthTokenService;
import com.example.auth.store.service.TokenReIssueResponse;
import com.example.auth.util.AuthRequestToken;
import com.example.auth.util.AuthUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JwtAuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthorizationGatewayFilterFactory.Config> {
    private final AuthTokenService authTokenService;
    private final AuthUtil authUtil;

    public JwtAuthorizationGatewayFilterFactory(@Autowired AuthTokenService authTokenService,
                                                @Autowired AuthUtil authUtil) {
        super(Config.class);
        this.authTokenService = authTokenService;
        this.authUtil = authUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            AuthRequestToken authRequestToken = authUtil.reactiveExportAuthorization(request);
            if (StringUtils.hasText(authRequestToken.getAccessToken())) {
                if (StringUtils.hasText(authRequestToken.getRefreshToken())) {
                    Optional<TokenReIssueResponse> tokenReIssueResponseOpt = authTokenService.reIssueToken(authRequestToken.getAccessToken(), authRequestToken.getRefreshToken());
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

                Optional<AuthDataInformation> authDataInformationOpt = authTokenService.getAuthDataInformation(authRequestToken.getAccessToken());

                if (authDataInformationOpt.isPresent()) {
                    AuthDataInformation authDataInformation = authDataInformationOpt.get();
                    AbstractAuthenticationToken authenticationToken = createAuthenticationToken(authDataInformation);

                    if (config.roles != null) {
                        List<String> cloneRoles = new java.util.ArrayList<>(authDataInformation.getRoles());
                        cloneRoles.retainAll(config.roles);
                        if (cloneRoles.isEmpty()) {
                            return onError(response, "권한이 존재하지 않습니다.", HttpStatus.FORBIDDEN);
                        }
                    }
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
                }
            }
            return onError(response, "인증에 실패하였습니다.", HttpStatus.FORBIDDEN);
        };
    }

    private AbstractAuthenticationToken createAuthenticationToken(AuthDataInformation authDataInformation) {
        User user = new User(authDataInformation.getDataSignKey(), authDataInformation.getRoles());
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
        response.setStatusCode(status);
        DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Setter
    public static class Config {
        private List<String> roles;
    }
}
