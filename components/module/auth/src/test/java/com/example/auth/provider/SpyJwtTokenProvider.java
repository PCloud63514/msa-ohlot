package com.example.auth.provider;

import com.example.token.jwt.domain.JwtToken;
import com.example.token.jwt.provider.JwtTokenGenerateRequest;
import com.example.token.jwt.provider.JwtTokenProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpyJwtTokenProvider extends JwtTokenProvider {
    public Long generate_validity_argument;
    public Long generate_refreshValidity_argument;
    public JwtToken generate_returnValue;
    public List<String> isExpiration_token_argument = new ArrayList<>();
    public Map<String, Boolean> isExpiration_returnValue = new HashMap<>(); // false = 만료

    public SpyJwtTokenProvider() {
        super(null, null, null);
    }

    @Override
    public JwtToken generate(JwtTokenGenerateRequest request) {
        this.generate_validity_argument = request.getAccessTokenValidity();
        this.generate_refreshValidity_argument = request.getRefreshTokenValidity();

        return generate_returnValue;
    }

    @Override
    public boolean isExpiration(String token) {
        this.isExpiration_token_argument.add(token);
        if (!isExpiration_returnValue.containsKey(token)) {
            isExpiration_returnValue.put(token, false);
        }
        return isExpiration_returnValue.get(token);
    }
}
