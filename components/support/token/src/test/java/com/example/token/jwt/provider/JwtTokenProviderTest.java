package com.example.token.jwt.provider;

import com.example.token.jwt.config.properties.JwtProperties;
import com.example.token.jwt.domain.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {
    JwtTokenProvider jwtTokenProvider;
    StubUuidProvider stubUuidProvider;
    StubDateProvider stubDateProvider;
    JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecretKey("secretKey");
        stubUuidProvider = new StubUuidProvider();
        stubDateProvider = new StubDateProvider();
        jwtTokenProvider = new JwtTokenProvider(jwtProperties, stubUuidProvider, stubDateProvider);
    }

    @Test
    void generate_returnValue() {
        long givenValidity = 10000L;
        long givenRefreshValidity = 100000L;

        UUID uuid1 = stubUuidProvider.randomUUID();
        Date date = stubDateProvider.now();
        Claims claims1 = Jwts.claims().setSubject(uuid1.toString());
        String accessToken = Jwts.builder()
                .setClaims(claims1)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + givenValidity))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        UUID uuid2 = stubUuidProvider.randomUUID();
        Claims claims = Jwts.claims().setSubject(uuid2.toString());
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + givenRefreshValidity))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        stubUuidProvider.index = 0;

        JwtToken jwtToken = jwtTokenProvider.generate(new JwtTokenGenerateRequest(givenValidity, givenRefreshValidity));

        assertThat(jwtToken.accessToken()).isEqualTo(accessToken);
        assertThat(jwtToken.refreshToken()).isEqualTo(refreshToken);
        assertThat(jwtToken.getInformation().getAccessToken()).isEqualTo(jwtToken.accessToken());
        assertThat(jwtToken.getInformation().getRefreshToken()).isEqualTo(jwtToken.refreshToken());
        assertThat(jwtToken.getInformation().getAccessTokenValidity()).isEqualTo(givenValidity);
        assertThat(jwtToken.getInformation().getRefreshTokenValidity()).isEqualTo(givenRefreshValidity);
    }
}