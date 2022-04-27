package com.example.auth.api;

import com.example.auth.AuthUtil;
import com.example.auth.service.AuthService;
import com.example.auth.service.TokenGenerateRequest;
import com.example.auth.service.TokenGenerateResponse;
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

import static com.example.auth.AuthUtil.ACCESS_TOKEN_SYNTAX;
import static com.example.auth.AuthUtil.REFRESH_TOKEN_SYNTAX;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthApi {
    private final AuthService authService;
    private final AuthUtil authUtil;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Mono<TokenIssueResponse> issueToken(@Valid @RequestBody TokenGenerateRequest request) {
        TokenGenerateResponse response = authService.generateToken(request);
        return Mono.just(new TokenIssueResponse(response.getAccessToken(), response.getRefreshToken()));
    }
    // 맞다 여기는 사용자가 접근하는게 아니라 다르다
    @DeleteMapping
    public void breakToken(ServerHttpRequest request) {
        String accessToken = Optional.ofNullable(request.getHeaders()
                        .getFirst(ACCESS_TOKEN_SYNTAX))
                .orElseThrow(RuntimeException::new);
        String refreshToken = Optional.ofNullable(request.getHeaders()
                        .getFirst(REFRESH_TOKEN_SYNTAX))
                .orElseThrow(RuntimeException::new);
        authService.deleteToken(accessToken, refreshToken);
    }
}
