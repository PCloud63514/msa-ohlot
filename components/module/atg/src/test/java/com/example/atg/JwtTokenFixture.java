package com.example.atg;

import com.example.token.jwt.domain.JwtToken;
import com.example.token.jwt.domain.JwtTokenInformation;

import java.util.Date;

public class JwtTokenFixture {
    public static JwtToken anToken() {
        return new JwtToken(new JwtTokenInformation(
                "accessToken",
                "refreshToken",
                10000L,
                100000L,
                new Date()));
    }
}
