package com.example.auth.service;

public interface AuthService {
    TokenGenerateResponse generateToken(TokenGenerateRequest request);
    void deleteToken(String accessToken, String refreshToken);
//
//    Optional<AuthDataInformation> getAuthDataInformation(String token);
//
//    Optional<JwtToken> reIssueToken(String token, String refresh);
}
