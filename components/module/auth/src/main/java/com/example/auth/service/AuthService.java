package com.example.auth.service;

public interface AuthService {
    /**
     * HKey 발급
     */
    CryptGenerateResponse generateCrypt();

    /**
     * 인증
     */
    MemberAuthenticationResponse authenticationMember(MemberAuthenticationRequest request, String crypt);
}
