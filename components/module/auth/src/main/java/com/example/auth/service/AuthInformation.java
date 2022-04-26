package com.example.auth.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthInformation {
    private List<String> roles;
    private String dataSignKey;
    private Long accessTokenValidity;
    private Long refreshTokenValidity;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}

