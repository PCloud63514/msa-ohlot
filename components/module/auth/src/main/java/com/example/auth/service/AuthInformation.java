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
    private Long validity;
    private Long refreshValidity;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime createAt;
}

