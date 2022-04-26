package com.example.ohlot.service;

import com.example.ohlot.domain.GoodWord;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoodWordGetResponse {
    private String id;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    public GoodWordGetResponse(GoodWord goodWord) {
        this.id = goodWord.getId().toString();
        this.content = goodWord.getContent();
        this.createAt = goodWord.getCreateAt();
        this.updateAt = goodWord.getUpdateAt();
    }
}
