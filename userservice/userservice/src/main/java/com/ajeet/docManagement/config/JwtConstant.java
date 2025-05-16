package com.ajeet.docManagement.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtConstant {
	//public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	  // Use a fixed secret string (must be at least 32+ characters for HS256)
    public static final String SECRET = "YRe+liDwxJOYpcU/icfoVHxBrXgvLEk5u+twLM+hhVc=";

    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	public static final String JWT_HEADER = "Authorization";
}
