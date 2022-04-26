package com.example.auth.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenGenerateResponse {
    private final String accessToken;
    private final String refreshToken;
}
