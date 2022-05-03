package com.example.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static String encrypt(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            for (byte b : md.digest()) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
