package com.example.auth.service;

import com.example.auth.api.CryptGenerateResponse;
import org.springframework.web.server.WebSession;

public interface AuthService {
    /**
     * HKey 발급
     */
    CryptGenerateResponse generateCrypt(WebSession session);

    /**
     * 인증
     */
}
