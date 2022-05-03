package com.example.auth.service;

import com.example.auth.api.CryptGenerateResponse;
import com.example.simple.provider.SHA256Provider;
import com.example.simple.provider.UUIDProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.WebSession;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UUIDProvider uuidProvider;
    private final SHA256Provider sha256Provider;

    @Override
    public CryptGenerateResponse generateCrypt(WebSession session) {
        String uniqId = uuidProvider.randomUUID().toString();
        String encrypt = sha256Provider.encrypt(uniqId);
        return new CryptGenerateResponse(encrypt);
    }
}
