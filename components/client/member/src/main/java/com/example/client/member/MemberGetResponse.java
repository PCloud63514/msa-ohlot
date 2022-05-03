package com.example.client.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberGetResponse {
    private String id;
    private String password;
    private String name;
}

