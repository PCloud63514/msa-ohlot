package com.example.auth.service;

import java.util.Optional;

public interface AuthService {
    TokenGenerateResponse generateToken(TokenGenerateRequest request);
    void deleteToken(String accessToken, String refreshToken);
//
//    Optional<AuthDataInformation> getAuthDataInformation(String token);
//
    Optional<TokenReIssueResponse> reIssueToken(String accessToken, String refreshToken);
}
