package com.example.auth.service;

public class SpyAuthService implements AuthService {

    @Override
    public CryptGenerateResponse generateCrypt() {
        return new CryptGenerateResponse("crypt");
    }

    @Override
    public MemberAuthenticationResponse authenticationMember(MemberAuthenticationRequest request, String crypt) {
        return null;
    }
}
