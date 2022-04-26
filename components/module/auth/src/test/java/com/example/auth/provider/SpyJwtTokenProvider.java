package com.example.auth.provider;

import com.example.token.jwt.domain.JwtToken;
import com.example.token.jwt.provider.JwtTokenGenerateRequest;
import com.example.token.jwt.provider.JwtTokenProvider;

public class SpyJwtTokenProvider extends JwtTokenProvider {
    public Long generate_validity_argument;
    public Long generate_refreshValidity_argument;
    public JwtToken generate_returnValue;

    public SpyJwtTokenProvider() {
        super(null, null, null);
    }

    @Override
    public JwtToken generate(JwtTokenGenerateRequest request) {
        this.generate_validity_argument = request.getAccessTokenValidity();
        this.generate_refreshValidity_argument = request.getRefreshTokenValidity();

        return generate_returnValue;
    }
}
