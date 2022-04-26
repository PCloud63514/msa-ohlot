package com.example.ohlot.api;

import com.example.ohlot.service.GoodWordAddRequest;
import com.example.ohlot.service.SpyGoodWordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GoodWordApiTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private SpyGoodWordService spyGoodWordService;
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        spyGoodWordService = new SpyGoodWordService();
        mockMvc = MockMvcBuilders.standaloneSetup(new GoodWordApi(spyGoodWordService)).build();
    }

    @Test
    void addGoodWord_createdHttpStatus() throws Exception {
        mockMvc.perform(post("/good-words")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    void addGoodWord_returnValue() throws Exception {
        mockMvc.perform(post("/good-words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"content\":\"content1\" }"))
                .andExpect(jsonPath("$.id", equalTo("id")))
                .andExpect(jsonPath("$.content", equalTo("content1")))
                .andExpect(status().isCreated());
    }

    @Test
    void addGoodWord_passesGoodWordAddRequestToGoodWordService() throws Exception {
        String givenContent = "givenContent";
        GoodWordAddRequest givenRequest = new GoodWordAddRequest(givenContent);

        mockMvc.perform(post("/good-words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(givenRequest)))
                .andExpect(jsonPath("$.id", equalTo("id")))
                .andExpect(jsonPath("$.content", equalTo(givenContent)))
                .andExpect(status().isCreated());

        assertThat(spyGoodWordService.addGoodWord_argument.getContent()).isEqualTo(givenContent);
    }

    @Test
    void getGoodWord_okHttpStatus() throws Exception {
        mockMvc.perform(get("/good-words"))
                .andExpect(status().isOk());
    }

    @Test
    void getGoodWord_returnValue() throws Exception {
        mockMvc.perform(get("/good-words"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", equalTo("id")))
                .andExpect(jsonPath("$[0].content", equalTo("content")))
                .andExpect(jsonPath("$[0].createAt", equalTo("2022-02-02 22:22:22")))
                .andExpect(jsonPath("$[0].updateAt", equalTo("2022-02-02 22:22:22")))
                .andExpect(status().isOk());

        assertThat(spyGoodWordService.getGoodWords_wasGetGoodWords).isTrue();
    }

    @Test
    void updateGoodWord_okHttpStatus() throws Exception {
        mockMvc.perform(patch("/good-words/{id}", UUID.randomUUID())
                        .param("content", "updateContent"))
                .andExpect(status().isOk());
    }

    @Test
    void updateGoodWord_passesRequestByUpdateGoodWordOfService() throws Exception {
        UUID givenId = UUID.randomUUID();
        String givenUpdateContent = "updateContent";

        mockMvc.perform(patch("/good-words/{id}", givenId)
                        .param("content", givenUpdateContent))
                .andExpect(status().isOk());

        assertThat(spyGoodWordService.updateGoodWord_argument.getId()).isEqualTo(givenId);
        assertThat(spyGoodWordService.updateGoodWord_argument.getContent()).isEqualTo(givenUpdateContent);
    }

    @Test
    void deleteGoodWord_okHttpStatus() throws Exception {
        mockMvc.perform(delete("/good-words/{id}", UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteGoodWord_passesIdToGoodWordService() throws Exception {
        UUID givenId = UUID.randomUUID();
        mockMvc.perform(delete("/good-words/{id}", givenId))
                .andExpect(status().isOk());

        assertThat(spyGoodWordService.deleteGoodWord_argument).isEqualTo(givenId);
    }
}