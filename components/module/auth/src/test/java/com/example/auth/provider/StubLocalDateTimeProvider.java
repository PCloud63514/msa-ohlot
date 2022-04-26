package com.example.auth.provider;

import com.example.auth.provider.LocalDateTimeProvider;

import java.time.LocalDateTime;

public class StubLocalDateTimeProvider extends LocalDateTimeProvider {
    public LocalDateTime now = LocalDateTime.now();
    @Override
    public LocalDateTime now() {
        return this.now;
    }
}

