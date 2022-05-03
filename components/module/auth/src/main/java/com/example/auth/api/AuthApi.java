package com.example.auth.api;

import com.example.auth.service.AuthService;
import com.example.auth.service.CryptGenerateResponse;
import com.example.auth.service.MemberAuthenticationRequest;
import com.example.auth.service.MemberAuthenticationResponse;
import com.example.auth.store.service.AuthTokenService;
import com.example.auth.store.service.TokenGenerateRequest;
import com.example.auth.store.service.TokenGenerateResponse;
import com.example.auth.util.AuthUtil;
import com.example.crypto.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthApi {
    private final AuthService authService;
    private final AuthTokenService authTokenService;
    private final AuthUtil authUtil;


    @GetMapping("authTest")
    public Mono<String> authTest(@SessionAttribute("crypt") String crypt, @RequestParam("pw") String password) {
        String encodePassword = Base64.getEncoder().encodeToString(SHA256.encrypt(password).getBytes(StandardCharsets.UTF_8));
        return Mono.just(SHA256.encrypt(encodePassword, crypt));
    }

    @PostMapping("member")
    public Mono<Void> authenticationMember(@SessionAttribute("crypt") String crypt, ServerHttpResponse serverHttpResponse, @RequestBody MemberAuthenticationRequest request) {
        MemberAuthenticationResponse response = authService.authenticationMember(request, crypt);
        Map<String, Object> data = new HashMap<>();
        data.put("id", response.getId());
        data.put("name", response.getName());
        TokenGenerateRequest tokenGenerateRequest = new TokenGenerateRequest(
                List.of("ROLE_MEMBER"), data, 60L * 1000L, 7L * 24L * 60L * 60L * 1000L
        );
        TokenGenerateResponse tokenGenerateResponse = authTokenService.generateToken(tokenGenerateRequest);
        authUtil.reactiveInjectAuthorization(
                tokenGenerateResponse.getAccessToken(),
                tokenGenerateResponse.getRefreshToken(),
                serverHttpResponse);

        return Mono.empty();
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
