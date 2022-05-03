package com.example.auth.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberAuthenticationResponse {
    private final String id;
    private final String name;
}
