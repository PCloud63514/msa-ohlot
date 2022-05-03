package com.example.client.member;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-client", url = "${client.member.url}")
public interface MemberClient {
    @GetMapping
    MemberGetResponse getMember(@RequestParam(name = "id") String id);
}
