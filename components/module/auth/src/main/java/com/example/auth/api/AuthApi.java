package com.example.auth.api;

import com.example.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthApi {
    private final AuthService authService;

    @PostMapping("member")
    public void authenticationMember() {

    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @GetMapping("crypt")
    public Mono<CryptGenerateResponse> createCrypt(WebSession webSession) {
        return Mono.just(authService.generateCrypt(webSession));
    }
}
