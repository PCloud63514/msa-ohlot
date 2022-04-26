package com.example.auth.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenGenerateRequest {
    @NotNull
    private List<@Pattern(regexp = "^ROLE_\\w{1,20}$") String> roles;
    private Map<String, Object> data;
    @NotNull
    private Long validity;
    @NotNull
    private Long refreshValidity;
}
