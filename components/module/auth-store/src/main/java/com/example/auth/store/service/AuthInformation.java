package com.example.auth.store.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthInformation implements Serializable {
    private List<String> roles;
    private String dataSignKey;
    private Long accessTokenValidity;
    private Long refreshTokenValidity;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}

