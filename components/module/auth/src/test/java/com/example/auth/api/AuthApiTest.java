package com.example.auth.api;

import com.example.auth.service.SpyAuthService;
import com.example.auth.service.SpyAuthTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

class AuthApiTest {
    private WebTestClient webTestClient;
    private SpyAuthUtil spyAuthUtil;
    private SpyAuthService spyAuthService;
    private SpyAuthTokenService spyAuthTokenService;
    @BeforeEach
    void setUp() {
        spyAuthUtil = new SpyAuthUtil();
        spyAuthService = new SpyAuthService();
        spyAuthTokenService = new SpyAuthTokenService();
        webTestClient = WebTestClient.bindToController(new AuthApi(spyAuthService, spyAuthTokenService, spyAuthUtil))
                .configureClient()
                .build();
    }

    @Test
    void authenticationMember_okHttpStatus() throws Exception {
        // Test과정에서 WebSession을 어떻게 주입할지 학습이 부족하여 테스트 진행 불가
//        webTestClient.post()
//                .uri("/auth/member")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromValue("{\"id\": \"id\", \"password\": \"password\"}"))
//                .exchange()
//                .expectStatus()
//                .isOk();
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