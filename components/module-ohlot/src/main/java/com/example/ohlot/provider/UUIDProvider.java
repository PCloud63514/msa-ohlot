package com.example.ohlot.provider;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDProvider {
    public UUID randomUUID() {
        return UUID.randomUUID();
    }
}
