package com.example.auth.service;

import com.example.auth.JwtTokenFixture;
import com.example.auth.provider.SpyJwtTokenProvider;
import com.example.auth.provider.StubLocalDateTimeProvider;
import com.example.auth.provider.StubUuidProvider;
import com.example.token.jwt.domain.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceImplTest {
    SpyJwtTokenProvider spyJwtTokenProvider;
    AuthServiceImpl authService;
    SpyRedisTemplate spyRedisTemplate;
    StubLocalDateTimeProvider stubLocalDateTimeProvider;
    StubUuidProvider uuidProvider;

    @BeforeEach
    void setUp() {
        spyRedisTemplate = new SpyRedisTemplate();
        spyJwtTokenProvider = new SpyJwtTokenProvider();
        stubLocalDateTimeProvider = new StubLocalDateTimeProvider();
        uuidProvider = new StubUuidProvider();
        authService = new AuthServiceImpl(spyJwtTokenProvider, spyRedisTemplate, stubLocalDateTimeProvider, uuidProvider);
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

        assertThat(spyRedisTemplate.opsForValue_wasCall).isTrue();
        assertThat(spyRedisTemplate.opsForHash_wasCall).isTrue();
        assertThat(spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.accessToken()))
                .isEqualTo(spyJwtTokenProvider.generate_returnValue.refreshToken());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getRoles())
                .isEqualTo(givenRequest.getRoles());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getAccessTokenValidity())
                .isEqualTo(givenRequest.getValidity());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getRefreshTokenValidity())
                .isEqualTo(givenRequest.getRefreshValidity());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getAccessToken())
                .isEqualTo(spyJwtTokenProvider.generate_returnValue.accessToken());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getRefreshToken())
                .isEqualTo(spyJwtTokenProvider.generate_returnValue.refreshToken());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getCreateAt())
                .isEqualTo(stubLocalDateTimeProvider.now());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getUpdateAt())
                .isEqualTo(stubLocalDateTimeProvider.now());
        assertThat(spyRedisTemplate.spyHashOperations.putAll_key_argument).isEqualTo(uuidProvider.randomUUID.get(0).toString());
        assertThat(spyRedisTemplate.spyHashOperations.putAll_value_argument).isEqualTo(givenRequest.getData());
        assertThat(spyRedisTemplate.expire_validity_map.get(spyJwtTokenProvider.generate_returnValue.accessToken())).isEqualTo(givenRequest.getValidity());
        assertThat(spyRedisTemplate.expire_validity_map.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).isEqualTo(givenRequest.getRefreshValidity());
        assertThat(spyRedisTemplate.expire_validity_map.get(uuidProvider.randomUUID.get(0).toString())).isEqualTo(givenRequest.getRefreshValidity());
        assertThat(spyRedisTemplate.expire_timeUnit_map.get(spyJwtTokenProvider.generate_returnValue.accessToken())).isEqualTo(TimeUnit.MILLISECONDS);
        assertThat(spyRedisTemplate.expire_timeUnit_map.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).isEqualTo(TimeUnit.MILLISECONDS);
        assertThat(spyRedisTemplate.expire_timeUnit_map.get(uuidProvider.randomUUID.get(0).toString())).isEqualTo(TimeUnit.MILLISECONDS);
    }

    @Test
    void deleteToken_passesTokenToRedisTemplate() {
        String givenAccessToken = "accessToken";
        String givenRefreshToken = "refreshToken";

        authService.deleteToken(givenAccessToken, givenRefreshToken);

        assertThat(spyRedisTemplate.delete_argument).contains(givenAccessToken, givenRefreshToken);
    }

    @Test
    void getAuthDataInformation_returnValue() throws Exception {
        String givenToken = "accessToken";
        String givenRefreshToken = "refreshToken";
        long givenAccessTokenValidity = 10000L;
        long givenRefreshValidity = 100000L;
        AuthInformation givenAuthInformation = new AuthInformation(List.of("ROLE_ADMIN"), "dataSignKey", givenAccessTokenValidity, givenRefreshValidity, givenToken, givenRefreshToken, LocalDateTime.now(), LocalDateTime.now());
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenToken, givenRefreshToken);
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenRefreshToken, givenAuthInformation);
        HashMap<String, Object> givenData = new HashMap<>();
        spyRedisTemplate.spyHashOperations.entries_returnValue = givenData;

        Optional<AuthDataInformation> authDataInformationOpt = authService.getAuthDataInformation(givenToken);

        assertThat(authDataInformationOpt.isPresent()).isTrue();

        AuthDataInformation authDataInformation = authDataInformationOpt.get();
        assertThat(authDataInformation.getDataSignKey()).isEqualTo(givenAuthInformation.getDataSignKey());
        assertThat(authDataInformation.getRoles()).isEqualTo(givenAuthInformation.getRoles());
        assertThat(authDataInformation.getData()).isEqualTo(givenData);
    }

    @Test
    void getAuthDataInformation_passesAccessToken_To_IsExpiration_Of_JwtTokenProvider() throws Exception {
        String givenToken = "accessToken";
        spyJwtTokenProvider.isExpiration_returnValue.put(givenToken, true);

        Optional<AuthDataInformation> authDataInformation = authService.getAuthDataInformation(givenToken);

        assertThat(spyJwtTokenProvider.isExpiration_token_argument).contains(givenToken);
        assertThat(authDataInformation.isEmpty()).isTrue();
    }

    @Test
    void getAuthDataInformation_returnValue_Empty_To_passesAccessToken() throws Exception {
        String givenToken = "accessToken";

        Optional<AuthDataInformation> authDataInformation = authService.getAuthDataInformation(givenToken);

        assertThat(spyRedisTemplate.spyValueOperations.get_argument).contains(givenToken);
        assertThat(authDataInformation.isEmpty()).isTrue();
    }

    @Test
    void getAuthDataInformation_returnValue_Empty_To_passesRefreshToken() throws Exception {
        String givenToken = "accessToken";
        String givenRefresh = "refresh";
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenToken, givenRefresh);

        Optional<AuthDataInformation> authDataInformation = authService.getAuthDataInformation(givenToken);

        assertThat(spyRedisTemplate.spyValueOperations.get_argument).contains(givenToken, givenRefresh);
        assertThat(authDataInformation.isEmpty()).isTrue();
    }

    @Test
    void getAuthDataInformation_passesDataSignKey_To_Entries_Of_ForHash() throws Exception {
        String givenToken = "accessToken";
        String givenRefreshToken = "refreshToken";
        long givenAccessTokenValidity = 10000L;
        long givenRefreshValidity = 100000L;
        AuthInformation givenAuthInformation = new AuthInformation(List.of("ROLE_ADMIN"), "dataSignKey", givenAccessTokenValidity, givenRefreshValidity, givenToken, givenRefreshToken, LocalDateTime.now(), LocalDateTime.now());
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenToken, givenRefreshToken);
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenRefreshToken, givenAuthInformation);

        Optional<AuthDataInformation> authDataInformationOpt = authService.getAuthDataInformation(givenToken);

        assertThat(spyRedisTemplate.spyHashOperations.entries_argument).isEqualTo(givenAuthInformation.getDataSignKey());
    }

    @Test
    void reIssueToken_returnValue() throws Exception {
        String givenAccessToken = "AccessToken";
        String givenRefreshToken = "refreshToken";
        long givenAccessTokenValidity = 10000L;
        long givenRefreshTokenValidity = 100000L;
        AuthInformation givenAuthInformation = new AuthInformation(null, null, givenAccessTokenValidity, givenRefreshTokenValidity, givenAccessToken, null, null, null);
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenRefreshToken, givenAuthInformation);
        spyJwtTokenProvider.isExpiration_returnValue.put(givenAccessToken, true);
        spyJwtTokenProvider.generate_returnValue = JwtTokenFixture.anToken();

        Optional<TokenReIssueResponse> response = authService.reIssueToken(givenAccessToken, givenRefreshToken);

        assertThat(response.isEmpty()).isFalse();
        assertThat(response.get().getAccessToken()).isEqualTo(spyJwtTokenProvider.generate_returnValue.accessToken());
        assertThat(response.get().getRefreshToken()).isEqualTo(spyJwtTokenProvider.generate_returnValue.refreshToken());
    }

    @Test
    void reIssueToken_passesRefreshTokenToJwtTokenProvider() {
        String givenRefreshToken = "refreshToken";
        spyJwtTokenProvider.isExpiration_returnValue.put(givenRefreshToken, true);

        authService.reIssueToken(null, givenRefreshToken);

        assertThat(spyJwtTokenProvider.isExpiration_token_argument).contains(givenRefreshToken);
    }

    @Test
    void reIssueToken_refreshTokenIsExpirationTheReturnValueEmpty() throws Exception {
        String givenRefreshToken = "refreshToken";
        spyJwtTokenProvider.isExpiration_returnValue.put(givenRefreshToken, true);

        Optional<TokenReIssueResponse> responseOpt = authService.reIssueToken(null, givenRefreshToken);

        assertThat(responseOpt.isEmpty()).isTrue();
    }

    @Test
    void reIssueToken_nullOfOpValueGet_The_ReturnValueEmpty() throws Exception {
        String givenRefreshToken = "refreshToken";

        Optional<TokenReIssueResponse> response = authService.reIssueToken(null, givenRefreshToken);

        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    void reIssueToken_accessToken_NotEquals_AccessTokenOfAuthInformation_The_ReturnValueEmpty() throws Exception {
        String givenAccessToken = "FakeAccessToken";
        String givenRefreshToken = "refreshToken";
        AuthInformation givenAuthInformation = new AuthInformation(null, null, null, null, "AccessToken", null, null, null);
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenRefreshToken, givenAuthInformation);

        Optional<TokenReIssueResponse> response = authService.reIssueToken(givenAccessToken, givenRefreshToken);

        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    void reIssueToken_passesAccessToken_To_IsExpiration_Of_JwtTokenProvider() throws Exception {
        String givenAccessToken = "AccessToken";
        String givenRefreshToken = "refreshToken";
        AuthInformation givenAuthInformation = new AuthInformation(null, null, null, null, givenAccessToken, null, null, null);
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenRefreshToken, givenAuthInformation);
        spyJwtTokenProvider.isExpiration_returnValue.put(givenAccessToken, false);

        Optional<TokenReIssueResponse> response = authService.reIssueToken(givenAccessToken, givenRefreshToken);

        assertThat(response.isEmpty()).isFalse();
        assertThat(response.get().getAccessToken()).isEqualTo(givenAccessToken);
        assertThat(response.get().getRefreshToken()).isEqualTo(givenRefreshToken);
    }

    @Test
    void reIssueToken_passesValidity_To_Generate_Of_JwtTokenProvider() throws Exception {
        String givenAccessToken = "AccessToken";
        String givenRefreshToken = "refreshToken";
        long givenAccessTokenValidity = 10000L;
        long givenRefreshValidity = 100000L;
        AuthInformation givenAuthInformation = new AuthInformation(null, null, givenAccessTokenValidity, givenRefreshValidity, givenAccessToken, null, null, null);
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenRefreshToken, givenAuthInformation);
        spyJwtTokenProvider.isExpiration_returnValue.put(givenAccessToken, true);
        spyJwtTokenProvider.generate_returnValue = JwtTokenFixture.anToken();

        authService.reIssueToken(givenAccessToken, givenRefreshToken);

        assertThat(spyJwtTokenProvider.generate_validity_argument).isEqualTo(givenAccessTokenValidity);
        assertThat(spyJwtTokenProvider.generate_refreshValidity_argument).isEqualTo(givenRefreshValidity);
    }

    @Test
    void reIssueToken_passesToken_To_Set_Of_OpValue() throws Exception {
        String givenAccessToken = "AccessToken";
        String givenRefreshToken = "refreshToken";
        long givenAccessTokenValidity = 10000L;
        long givenRefreshValidity = 100000L;
        AuthInformation givenAuthInformation = new AuthInformation(List.of("ROLE_ADMIN"), "dataSignKey", givenAccessTokenValidity, givenRefreshValidity, givenAccessToken, givenRefreshToken, LocalDateTime.now(), LocalDateTime.now());
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenRefreshToken, givenAuthInformation);
        spyJwtTokenProvider.isExpiration_returnValue.put(givenAccessToken, true);
        spyJwtTokenProvider.generate_returnValue = JwtTokenFixture.anToken();

        authService.reIssueToken(givenAccessToken, givenRefreshToken);

        assertThat(spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.accessToken()))
                .isEqualTo(spyJwtTokenProvider.generate_returnValue.refreshToken());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getRoles())
                .isEqualTo(((AuthInformation)spyRedisTemplate.spyValueOperations.get_returnValue.get(givenRefreshToken)).getRoles());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getDataSignKey())
                .isEqualTo(((AuthInformation)spyRedisTemplate.spyValueOperations.get_returnValue.get(givenRefreshToken)).getDataSignKey());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getAccessTokenValidity())
                .isEqualTo(((AuthInformation)spyRedisTemplate.spyValueOperations.get_returnValue.get(givenRefreshToken)).getAccessTokenValidity());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getRefreshTokenValidity())
                .isEqualTo(((AuthInformation)spyRedisTemplate.spyValueOperations.get_returnValue.get(givenRefreshToken)).getRefreshTokenValidity());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getAccessToken())
                .isEqualTo(spyJwtTokenProvider.generate_returnValue.accessToken());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getRefreshToken())
                .isEqualTo(spyJwtTokenProvider.generate_returnValue.refreshToken());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getCreateAt())
                .isEqualTo(((AuthInformation)spyRedisTemplate.spyValueOperations.get_returnValue.get(givenRefreshToken)).getCreateAt());
        assertThat(((AuthInformation)spyRedisTemplate.spyValueOperations.set_argument.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).getUpdateAt())
                .isEqualTo(stubLocalDateTimeProvider.now());
    }

    @Test
    void reIssue_passesArgs_To_Delete_And_Expire_Of_RedisTemplate() throws Exception {
        String givenAccessToken = "AccessToken";
        String givenRefreshToken = "refreshToken";
        long givenAccessTokenValidity = 10000L;
        long givenRefreshValidity = 100000L;
        AuthInformation givenAuthInformation = new AuthInformation(List.of("ROLE_ADMIN"), "dataSignKey", givenAccessTokenValidity, givenRefreshValidity, givenAccessToken, givenRefreshToken, LocalDateTime.now(), LocalDateTime.now());
        spyRedisTemplate.spyValueOperations.get_returnValue.put(givenRefreshToken, givenAuthInformation);
        spyJwtTokenProvider.isExpiration_returnValue.put(givenAccessToken, true);
        spyJwtTokenProvider.generate_returnValue = JwtTokenFixture.anToken();

        authService.reIssueToken(givenAccessToken, givenRefreshToken);

        assertThat(spyRedisTemplate.delete_argument).contains(givenAccessToken, givenRefreshToken);

        assertThat(spyRedisTemplate.expire_validity_map.get(spyJwtTokenProvider.generate_returnValue.accessToken())).isEqualTo(((AuthInformation)spyRedisTemplate.spyValueOperations.get_returnValue.get(givenRefreshToken)).getAccessTokenValidity());
        assertThat(spyRedisTemplate.expire_validity_map.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).isEqualTo(((AuthInformation)spyRedisTemplate.spyValueOperations.get_returnValue.get(givenRefreshToken)).getRefreshTokenValidity());
        assertThat(spyRedisTemplate.expire_validity_map.get(((AuthInformation)spyRedisTemplate.spyValueOperations.get_returnValue.get(givenRefreshToken)).getDataSignKey())).isEqualTo(((AuthInformation)spyRedisTemplate.spyValueOperations.get_returnValue.get(givenRefreshToken)).getRefreshTokenValidity());
        assertThat(spyRedisTemplate.expire_timeUnit_map.get(spyJwtTokenProvider.generate_returnValue.accessToken())).isEqualTo(TimeUnit.MILLISECONDS);
        assertThat(spyRedisTemplate.expire_timeUnit_map.get(spyJwtTokenProvider.generate_returnValue.refreshToken())).isEqualTo(TimeUnit.MILLISECONDS);
        assertThat(spyRedisTemplate.expire_timeUnit_map.get(((AuthInformation)spyRedisTemplate.spyValueOperations.get_returnValue.get(givenRefreshToken)).getDataSignKey())).isEqualTo(TimeUnit.MILLISECONDS);
    }
}