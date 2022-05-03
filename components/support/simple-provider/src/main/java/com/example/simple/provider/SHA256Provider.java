package com.example.simple.provider;

import com.example.crypto.SHA256;
import org.springframework.stereotype.Component;


@Component
public class SHA256Provider {
    public String encrypt(String text) {
        return SHA256.encrypt(text);
    }
}
