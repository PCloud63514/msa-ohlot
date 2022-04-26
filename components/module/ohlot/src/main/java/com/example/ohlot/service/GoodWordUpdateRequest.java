package com.example.ohlot.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoodWordUpdateRequest {
    private UUID id;
    private String content;
}
