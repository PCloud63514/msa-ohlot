package com.example.token.jwt.provider;

import com.example.token.provider.TokenGenerateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtTokenGenerateRequest extends TokenGenerateRequest {
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
}
