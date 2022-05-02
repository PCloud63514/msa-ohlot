package com.example.member.api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberApiTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MemberApi()).build();
    }

    @Test
    void getMember_okHttpStatus() throws Exception {
        mockMvc.perform(get("/members")
                        .param("id", "id"))
                .andExpect(status().isOk());
    }

    @Test
    void getMember_returnValue() throws Exception {
        mockMvc.perform(get("/members")
                        .param("id", "id"))
                .andExpect(jsonPath("$.id", equalTo("id")))
                .andExpect(jsonPath("$.password", equalTo("password")))
                .andExpect(jsonPath("$.name", equalTo("name")))
        ;
    }
}