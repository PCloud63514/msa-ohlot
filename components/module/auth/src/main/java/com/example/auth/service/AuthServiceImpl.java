package com.example.auth.service;

import com.example.auth.provider.LocalDateTimeProvider;
import com.example.token.jwt.domain.JwtToken;
import com.example.token.jwt.provider.JwtTokenGenerateRequest;
import com.example.token.jwt.provider.JwtTokenProvider;
import com.example.token.provider.UuidProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final LocalDateTimeProvider localDateTimeProvider;
    private final UuidProvider uuidProvider;

    @Override
    public TokenGenerateResponse generateToken(TokenGenerateRequest request) {
        JwtToken jwtToken = jwtTokenProvider.generate(new JwtTokenGenerateRequest(request.getValidity(), request.getRefreshValidity()));
        String dataSignKey = uuidProvider.randomUUID().toString();

        LocalDateTime now = localDateTimeProvider.now();
        AuthInformation authInformation = new AuthInformation(
                request.getRoles(), dataSignKey,
                request.getValidity(), request.getRefreshValidity(),
                jwtToken.accessToken(), jwtToken.refreshToken(),
                now, now);

        ValueOperations<String, Object> opsValue = redisTemplate.opsForValue();
        opsValue.set(jwtToken.accessToken(), jwtToken.refreshToken());
        opsValue.set(jwtToken.refreshToken(), authInformation);

        HashOperations<String, String, Object> opsHash = redisTemplate.opsForHash();
        opsHash.putAll(dataSignKey, request.getData());

        redisTemplate.expire(jwtToken.accessToken(), request.getValidity(), TimeUnit.MILLISECONDS);
        redisTemplate.expire(jwtToken.refreshToken(), request.getRefreshValidity(), TimeUnit.MILLISECONDS);
        redisTemplate.expire(dataSignKey, request.getRefreshValidity(), TimeUnit.MILLISECONDS);

        return new TokenGenerateResponse(jwtToken.accessToken(), jwtToken.refreshToken());
    }

    @Override
    public void deleteToken(String accessToken, String refreshToken) {
        redisTemplate.delete(accessToken);
        redisTemplate.delete(refreshToken);
    }

    @Override
    public Optional<AuthDataInformation> getAuthDataInformation(String accessToken) {
        if (jwtTokenProvider.isExpiration(accessToken)) return Optional.empty();
        ValueOperations<String, Object> opValue = redisTemplate.opsForValue();
        String refreshToken = (String) opValue.get(accessToken);
        if (!StringUtils.hasText(refreshToken)) return Optional.empty();

        AuthInformation authInformation = (AuthInformation) opValue.get(refreshToken);
        if(authInformation == null) return Optional.empty();

        HashOperations<String, String, Object> opHash = redisTemplate.opsForHash();
        Map<String, Object> data = opHash.entries(authInformation.getDataSignKey());

        return Optional.of(new AuthDataInformation(authInformation.getDataSignKey(), authInformation.getRoles(), data));
    }

    @Override
    public Optional<TokenReIssueResponse> reIssueToken(String accessToken, String refreshToken) {
        if (jwtTokenProvider.isExpiration(refreshToken)) return Optional.empty();
        ValueOperations<String, Object> opValue = redisTemplate.opsForValue();
        AuthInformation authInformation = (AuthInformation) opValue.get(refreshToken);
        if (authInformation == null || !accessToken.equals(authInformation.getAccessToken())) return Optional.empty();
        if (!jwtTokenProvider.isExpiration(accessToken)) {
            return Optional.of(new TokenReIssueResponse(accessToken, refreshToken));
        }

        JwtToken newJwtToken = jwtTokenProvider.generate(new JwtTokenGenerateRequest(authInformation.getAccessTokenValidity(), authInformation.getRefreshTokenValidity()));
        opValue.set(newJwtToken.accessToken(), newJwtToken.refreshToken());
        AuthInformation newAuthInformation = new AuthInformation(
                authInformation.getRoles(), authInformation.getDataSignKey(),
                authInformation.getAccessTokenValidity(), authInformation.getRefreshTokenValidity(),
                newJwtToken.accessToken(), newJwtToken.refreshToken(), authInformation.getCreateAt(), localDateTimeProvider.now());
        opValue.set(newJwtToken.refreshToken(), newAuthInformation);

        redisTemplate.delete(accessToken);
        redisTemplate.delete(refreshToken);

        redisTemplate.expire(newJwtToken.accessToken(), newAuthInformation.getAccessTokenValidity(), TimeUnit.MILLISECONDS);
        redisTemplate.expire(newJwtToken.refreshToken(), newAuthInformation.getRefreshTokenValidity(), TimeUnit.MILLISECONDS);
        redisTemplate.expire(newAuthInformation.getDataSignKey(), newAuthInformation.getRefreshTokenValidity(), TimeUnit.MILLISECONDS);

        return  Optional.of(new TokenReIssueResponse(newJwtToken.accessToken(), newJwtToken.refreshToken()));
    }
}
