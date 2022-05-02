package com.example.atg.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenIssueResponse {
    private final String accessToken;
    private final String refreshToken;
}
