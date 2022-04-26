package com.example.auth.service;

import com.example.auth.provider.LocalDateTimeProvider;
import com.example.token.jwt.domain.JwtToken;
import com.example.token.jwt.provider.JwtTokenGenerateRequest;
import com.example.token.jwt.provider.JwtTokenProvider;
import com.example.token.provider.UuidProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
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

        AuthInformation authInformation = new AuthInformation(
                request.getRoles(), dataSignKey,
                request.getValidity(), request.getRefreshValidity(),
                jwtToken.accessToken(), jwtToken.refreshToken(),
                localDateTimeProvider.now());

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
        // refresh는 조회해보고 연계 데이터도 삭제를 해주긴해야함. 물론 시간설정은 되어있으니 안해줘도 동작에 문제는 없음.
        redisTemplate.delete(accessToken);
        redisTemplate.delete(refreshToken);
    }
}
