package com.example.auth.provider;

import com.example.simple.provider.UUIDProvider;

import java.util.UUID;

public class StubUUIDProvider extends UUIDProvider {
    public UUID uuid = UUID.randomUUID();
    @Override
    public UUID randomUUID() {
        return uuid;
    }
}
