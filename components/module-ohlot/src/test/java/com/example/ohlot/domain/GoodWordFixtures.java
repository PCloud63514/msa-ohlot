package com.example.ohlot.domain;

import com.example.ohlot.domain.GoodWord.GoodWordBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

public class GoodWordFixtures {
    public static GoodWordBuilder anGoodWord() {
        return GoodWord.builder()
                .id(UUID.randomUUID())
                .content("content")
                .createAt(LocalDateTime.of(2022, 2, 22, 22, 22, 22))
                .updateAt(LocalDateTime.of(2021, 11, 11, 11, 11, 11))
                ;
    }
}
