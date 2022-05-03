package com.example.auth.store.provider;

import com.example.simple.provider.LocalDateTimeProvider;

import java.time.LocalDateTime;

public class StubLocalDateTimeProvider extends LocalDateTimeProvider {
    public LocalDateTime now = LocalDateTime.now();
    @Override
    public LocalDateTime now() {
        return this.now;
    }
}

