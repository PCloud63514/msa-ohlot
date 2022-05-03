package com.example.auth.provider;

import com.example.simple.provider.SHA256Provider;

public class StubSHA256Provider extends SHA256Provider {
    public String encrypt_argument;
    public String encrypt_returnValue = "encrypt";

    public String encrypt_hmac_text_argument;
    public String encrypt_hmac_crypt_argument;
    public String encrypt_hmac_returnValue = "hmac";

    @Override
    public String encrypt(String text) {
        this.encrypt_argument = text;
        return encrypt_returnValue;
    }

    @Override
    public String encrypt(String text, String crypt) {
        this.encrypt_hmac_text_argument = text;
        this.encrypt_hmac_crypt_argument = crypt;
        return encrypt_hmac_returnValue;
    }
}
