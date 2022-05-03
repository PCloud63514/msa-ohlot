package com.example.auth.api;

import com.example.auth.service.SpyAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

class AuthApiTest {
    private WebTestClient webTestClient;
    private SpyAuthService spyAuthService;
    @BeforeEach
    void setUp() {
        spyAuthService = new SpyAuthService();
        webTestClient = WebTestClient.bindToController(new AuthApi(spyAuthService))
                .configureClient()
                .build();
    }

    @Test
    void authenticationMember_okHttpStatus() throws Exception {
        webTestClient.post()
                .uri("/auth/member")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void generateCrypt_createdHttpStatus() throws Exception {
        webTestClient.get()
                .uri("/auth/crypt")
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void generateCrypt_returnValue() throws Exception {
        webTestClient.get()
                .uri("/auth/crypt")
                .exchange()
                .expectHeader().exists("X-AUTH-TOKEN")
                .expectBody()
                .jsonPath("$.crypt").isEqualTo("crypt");
    }
}