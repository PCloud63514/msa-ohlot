package com.example.token.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Token <I extends TokenInformation> {
    private final I information;
}
