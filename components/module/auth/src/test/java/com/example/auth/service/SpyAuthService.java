package com.example.auth.service;

import com.example.auth.api.CryptGenerateResponse;
import org.springframework.web.server.WebSession;

public class SpyAuthService implements AuthService {
    public WebSession generateCrypt_argument;

    @Override
    public CryptGenerateResponse generateCrypt(WebSession session) {
        this.generateCrypt_argument = session;
        return new CryptGenerateResponse("crypt");
    }
}
