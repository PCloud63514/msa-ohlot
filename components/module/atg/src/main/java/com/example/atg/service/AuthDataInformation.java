package com.example.atg.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthDataInformation {
    private String dataSignKey;
    private List<@Pattern(regexp = "^ROLE_\\w{1,20}$") String> roles;
    private Map<String, Object> data;
}
