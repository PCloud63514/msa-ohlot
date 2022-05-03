package com.example.atg.api;

import com.example.atg.service.AuthTokenService;
import com.example.atg.service.TokenGenerateRequest;
import com.example.atg.service.TokenGenerateResponse;
import com.example.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthApi {
    private final AuthTokenService authService;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Mono<TokenIssueResponse> issueToken(@Valid @RequestBody TokenGenerateRequest request) {
        TokenGenerateResponse response = authService.generateToken(request);
        return Mono.just(new TokenIssueResponse(response.getAccessToken(), response.getRefreshToken()));
    }

    @DeleteMapping
    public void breakToken(ServerHttpRequest request) {
        String accessToken = Optional.ofNullable(request.getHeaders()
                        .getFirst(AuthUtil.ACCESS_TOKEN_SYNTAX))
                .orElseThrow(RuntimeException::new);
        String refreshToken = Optional.ofNullable(request.getHeaders()
                        .getFirst(AuthUtil.REFRESH_TOKEN_SYNTAX))
                .orElseThrow(RuntimeException::new);
        authService.deleteToken(accessToken, refreshToken);
    }
}
