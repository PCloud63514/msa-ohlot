package com.example.auth.service;

import com.example.auth.api.CryptGenerateResponse;
import com.example.auth.provider.StubSHA256Provider;
import com.example.auth.provider.StubUUIDProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceImplTest {
    AuthServiceImpl authServiceImpl;
    StubUUIDProvider stubUUIDProvider;
    StubSHA256Provider stubSHA256Provider;

    @BeforeEach
    void setUp() {
        stubUUIDProvider = new StubUUIDProvider();
        stubSHA256Provider = new StubSHA256Provider();
        authServiceImpl = new AuthServiceImpl(stubUUIDProvider, stubSHA256Provider);
    }

    @Test
    void generateCrypt_returnValue() throws Exception {
        CryptGenerateResponse response = authServiceImpl.generateCrypt();

        assertThat(stubSHA256Provider.encrypt_argument).isEqualTo(stubUUIDProvider.randomUUID().toString());
        assertThat(response.getCrypt()).isEqualTo(stubSHA256Provider.encrypt_returnValue);
    }
}