package com.example.member.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberGetResponse {
    private String id;
    private String password;
    private String name;
}
