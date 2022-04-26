package com.example.auth.service;

import com.example.auth.JwtTokenFixture;
import com.example.auth.provider.SpyJwtTokenProvider;
import com.example.auth.provider.StubLocalDateTimeProvider;
import com.example.auth.provider.StubUuidProvider;
import com.example.token.jwt.domain.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceImplTest {
    SpyJwtTokenProvider spyJwtTokenProvider;
    AuthServiceImpl authService;
    SpyRedisTemplate redisTemplate;
    StubLocalDateTimeProvider localDateTimeProvider;
    StubUuidProvider uuidProvider;

    @BeforeEach
    void setUp() {
        redisTemplate = new SpyRedisTemplate();
        spyJwtTokenProvider = new SpyJwtTokenProvider();
        localDateTimeProvider = new StubLocalDateTimeProvider();
        uuidProvider = new StubUuidProvider();
        authService = new AuthServiceImpl(spyJwtTokenProvider, redisTemplate, localDateTimeProvider, uuidProvider);
    }

    @Test
    void generateToken_returnValue() {
        JwtToken givenJwtToken = JwtTokenFixture.anToken();
        TokenGenerateRequest givenRequest = new TokenGenerateRequest(List.of(), null,
                givenJwtToken.getInformation().getAccessTokenValidity(),
                givenJwtToken.getInformation().getRefreshTokenValidity());
        spyJwtTokenProvider.generate_returnValue = givenJwtToken;

        TokenGenerateResponse tokenGenerateResponse = authService.generateToken(givenRequest);

        assertThat(tokenGenerateResponse.getAccessToken()).isEqualTo(spyJwtTokenProvider.generate_returnValue.accessToken());
        assertThat(tokenGenerateResponse.getRefreshToken()).isEqualTo(spyJwtTokenProvider.generate_returnValue.refreshToken());
    }

    @Test
    void generateToken_passesRequestToJwtTokenProvider() {
        JwtToken givenJwtToken = JwtTokenFixture.anToken();
        TokenGenerateRequest givenRequest = new TokenGenerateRequest(List.of(), null,
                givenJwtToken.getInformation().getAccessTokenValidity(),
                givenJwtToken.getInformation().getRefreshTokenValidity());
        spyJwtTokenProvider.generate_returnValue = givenJwtToken;

        authService.generateToken(givenRequest);

        assertThat(spyJwtTokenProvider.generate_validity_argument).isEqualTo(givenRequest.getValidity());
        assertThat(spyJwtTokenProvider.generate_refreshValidity_argument).isEqualTo(givenRequest.getRefreshValidity());
    }

    @Test
    void generateToken_passesRequestToRedisTemplate() {
        List<@Pattern(regexp = "^ROLE_\\w{1,20}$") String> givenRoles = List.of("ROLE_TEST");
        JwtToken givenJwtToken = JwtTokenFixture.anToken();
        TokenGenerateRequest givenRequest = new TokenGenerateRequest(givenRoles, new HashMap<>(),
                givenJwtToken.getInformation().getAccessTokenValidity(),
                givenJwtToken.getInformation().getRefreshTokenValidity());
        spyJwtTokenProvider.generate_returnValue = givenJwtToken;

        authService.generateToken(givenRequest);

        assertThat(redisTemplate.opsForValue_wasCall).isTrue();
        assertThat(redisTemplate.opsForHash_wasCall).isTrue();
        assertThat(redisTemplate.spyValueOperations.set_key_argument.get(0)).isEqualTo(spyJwtTokenProvider.generate_returnValue.accessToken());
        assertThat(redisTemplate.spyValueOperations.set_value_argument.get(0)).isEqualTo(spyJwtTokenProvider.generate_returnValue.refreshToken());
        assertThat(redisTemplate.spyValueOperations.set_key_argument.get(1)).isEqualTo(spyJwtTokenProvider.generate_returnValue.refreshToken());
        assertThat(((AuthInformation) redisTemplate.spyValueOperations.set_value_argument.get(1)).getAccessToken()).isEqualTo(spyJwtTokenProvider.generate_returnValue.accessToken());
        assertThat(((AuthInformation) redisTemplate.spyValueOperations.set_value_argument.get(1)).getRefreshToken()).isEqualTo(spyJwtTokenProvider.generate_returnValue.refreshToken());
        assertThat(((AuthInformation) redisTemplate.spyValueOperations.set_value_argument.get(1)).getValidity()).isEqualTo(givenRequest.getValidity());
        assertThat(((AuthInformation) redisTemplate.spyValueOperations.set_value_argument.get(1)).getRefreshValidity()).isEqualTo(givenRequest.getRefreshValidity());
        assertThat(((AuthInformation) redisTemplate.spyValueOperations.set_value_argument.get(1)).getRoles()).isEqualTo(givenRequest.getRoles());
        assertThat(((AuthInformation) redisTemplate.spyValueOperations.set_value_argument.get(1)).getDataSignKey()).isEqualTo(uuidProvider.randomUUID.get(0).toString());
        assertThat(((AuthInformation) redisTemplate.spyValueOperations.set_value_argument.get(1)).getCreateAt()).isEqualTo(localDateTimeProvider.now());
        assertThat(redisTemplate.spyHashOperations.putAll_key_argument).isEqualTo(uuidProvider.randomUUID.get(0).toString());
        assertThat(redisTemplate.spyHashOperations.putAll_value_argument).isEqualTo(givenRequest.getData());
        assertThat(redisTemplate.expire_key_argument.get(0)).isEqualTo(spyJwtTokenProvider.generate_returnValue.accessToken());
        assertThat(redisTemplate.expire_key_argument.get(1)).isEqualTo(spyJwtTokenProvider.generate_returnValue.refreshToken());
        assertThat(redisTemplate.expire_key_argument.get(2)).isEqualTo(uuidProvider.randomUUID.get(0).toString());
        assertThat(redisTemplate.expire_timeout_argument.get(0)).isEqualTo(givenRequest.getValidity());
        assertThat(redisTemplate.expire_timeout_argument.get(1)).isEqualTo(givenRequest.getRefreshValidity());
        assertThat(redisTemplate.expire_timeout_argument.get(2)).isEqualTo(givenRequest.getRefreshValidity());
        assertThat(redisTemplate.expire_unit_argument.get(0)).isEqualTo(TimeUnit.MILLISECONDS);
        assertThat(redisTemplate.expire_unit_argument.get(1)).isEqualTo(TimeUnit.MILLISECONDS);
        assertThat(redisTemplate.expire_unit_argument.get(2)).isEqualTo(TimeUnit.MILLISECONDS);
    }

    @Test
    void deleteToken_passesTokenToRedisTemplate() {
        String givenAccessToken = "accessToken";
        String givenRefreshToken = "refreshToken";

        authService.deleteToken(givenAccessToken, givenRefreshToken);

        assertThat(redisTemplate.delete_argument.get(0)).isEqualTo(givenAccessToken);
        assertThat(redisTemplate.delete_argument.get(1)).isEqualTo(givenRefreshToken);
    }
}