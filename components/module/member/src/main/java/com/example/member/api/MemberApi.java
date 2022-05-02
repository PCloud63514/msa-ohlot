package com.example.member.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("members")
@RestController
public class MemberApi {

    @GetMapping
    public MemberGetResponse getMember(@RequestParam(name = "id") String id) {
        return new MemberGetResponse("id", "password", "name");
    }
}
