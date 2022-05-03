package com.example.auth.api;

import com.example.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @GetMapping(value = "crypt")
    public Mono<ResponseEntity<CryptGenerateResponse>> createCrypt(WebSession webSession) {
        CryptGenerateResponse response = authService.generateCrypt();
        webSession.getAttributes().put("crypt", response.getCrypt());

        return Mono.just(ResponseEntity.status(HttpStatus.CREATED)
                .header("X-AUTH-TOKEN", webSession.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response));

    }
}
