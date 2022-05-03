package com.example.auth.service;

import com.example.auth.store.service.AuthDataInformation;
import com.example.auth.store.service.AuthTokenService;
import com.example.auth.store.service.TokenGenerateRequest;
import com.example.auth.store.service.TokenGenerateResponse;
import com.example.auth.store.service.TokenReIssueResponse;

import java.util.Optional;

public class SpyAuthTokenService implements AuthTokenService {
    @Override
    public TokenGenerateResponse generateToken(TokenGenerateRequest request) {
        return null;
    }

    @Override
    public void deleteToken(String accessToken, String refreshToken) {

    }

    @Override
    public Optional<AuthDataInformation> getAuthDataInformation(String accessToken) {
        return Optional.empty();
    }

    @Override
    public Optional<TokenReIssueResponse> reIssueToken(String accessToken, String refreshToken) {
        return Optional.empty();
    }
}
