package com.example.auth.provider;

import com.example.simple.provider.SHA256Provider;

public class StubSHA256Provider extends SHA256Provider {
    public String encrypt_argument;
    public String encrypt_returnValue = "encrypt";

    @Override
    public String encrypt(String text) {
        this.encrypt_argument = text;
        return encrypt_returnValue;
    }
}
