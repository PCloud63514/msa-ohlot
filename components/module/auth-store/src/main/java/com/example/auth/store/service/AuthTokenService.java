package com.example.auth.store.service;

import java.util.Optional;

public interface AuthTokenService {
    TokenGenerateResponse generateToken(TokenGenerateRequest request);
    void deleteToken(String accessToken, String refreshToken);

    Optional<AuthDataInformation> getAuthDataInformation(String accessToken);

    Optional<TokenReIssueResponse> reIssueToken(String accessToken, String refreshToken);
}
