package com.example.auth.service;

import com.example.auth.api.CryptGenerateResponse;

public class SpyAuthService implements AuthService {

    @Override
    public CryptGenerateResponse generateCrypt() {
        return new CryptGenerateResponse("crypt");
    }
}
