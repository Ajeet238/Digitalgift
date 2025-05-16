package com.ajeet.docManagement.config;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretGenerator {

    public static void main(String[] args) {
        byte[] secret = new byte[32]; // 32 bytes = 256 bits (recommended for HS256)
        new SecureRandom().nextBytes(secret);

        String base64Secret = Base64.getEncoder().encodeToString(secret);
        System.out.println("Generated Secret Key: " + base64Secret);
    }
}
