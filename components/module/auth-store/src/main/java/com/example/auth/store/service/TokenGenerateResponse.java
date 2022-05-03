package com.example.auth.store.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenGenerateResponse {
    private final String accessToken;
    private final String refreshToken;
}
