package com.ajeet.docManagement.config;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ajeet.docManagement.Entity.Token;
import com.ajeet.docManagement.Repository.TokenRepository;
import com.ajeet.docManagement.userDetailServiceimpl.UserDetailService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
		
		@Autowired	
		private TokenRepository tokenRepository;
		
		@Autowired
		private PasswordEncoder passwordEncoder;

		@Autowired
		private UserDetailService customUserService;
		
		// used for token generation for reset password by email
		
	    private final Map<String, String> tokenStore = new HashMap<>();
	    private final Map<String, Long> expiryStore = new HashMap<>();
	    private final long EXPIRY = 15 * 60 * 1000; // 15 minutes
	    
	    
    // Secret key generated using HS256
    private static final SecretKey KEY =  JwtConstant.SECRET_KEY; // For HS256 algorithm

    // Method to generate the token
   
    public String generateToken(String value, String loginType) {
        long now = System.currentTimeMillis();
        Date validity = new Date(now + 1000 * 60 * 60); // Token valid for 1 hour
        Optional<Token> checkSession = Optional.empty();
       String  claimType = "";
        if(loginType.equals("email")) {
        	claimType = "email";
        	checkSession = tokenRepository.findByEmail(value);
        }
        if(loginType.equals("username")) {
        	claimType = "username";
        	checkSession = tokenRepository.findByUsername(value);
        }
        if(loginType.equals("phone")) {
        	claimType = "phone";
        	checkSession = tokenRepository.findByPhone(value);
        } 
        
        if(checkSession.isPresent()) {
        	tokenRepository.delete(checkSession.get());
        }
        String token = Jwts.builder()
                .setSubject(value)  // Set roles/authorities
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .claim(claimType, value)                     // custom claim 
                .signWith(KEY, SignatureAlgorithm.HS256)  // Use HS256 for signing
                .compact();
        
        // Save the token in the database
        Token tokenEntity = new Token();
        
        if(loginType.equals("email")) {
        	
        	 tokenEntity.setEmail(value);;
        	checkSession = tokenRepository.findByEmail(value);
        }
        if(loginType.equals("username")) {
        	 tokenEntity.setUsername(value);
        	checkSession = tokenRepository.findByUsername(value);
        }
        if(loginType.equals("phone")) {
        	 tokenEntity.setPhone(value);
        	checkSession = tokenRepository.findByPhone(value);
        } 
        
       
        tokenEntity.setToken(token);
        tokenEntity.setIssuedAt(LocalDateTime.now());
        tokenEntity.setExpiresAt(LocalDateTime.now().plusHours(10));
        tokenEntity.setRevoked(false);

        tokenRepository.save(tokenEntity);
        
        return token;
    }
    

    // Method to validate and parse the token
    public static Claims validateToken(String token) {
        try {
            System.out.println("Token: " + token);
            return Jwts.parser()
                       .setSigningKey(KEY)
                       .build()
                       .parseClaimsJws(token)
                       .getBody();
        } catch (Exception e) {
            return null; // Gracefully fail, allow null check in controller
        }
    }

	// Authenticate Username and password;
    
	public Authentication authenticate(String username, String password) {
		Optional<UserDetails> userDetails = Optional.of(customUserService.loadUserByUsername(username));
		if (userDetails.isEmpty()) {
			throw new BadCredentialsException("Invalid User");
		}
		if (!passwordEncoder.matches(password, userDetails.get().getPassword())) {
			throw new BadCredentialsException("Invalid Password");

		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.get().getAuthorities());
	}

    // Method to extract the email from the token
    public String getEmailFromToken(String jwt) {
        // Remove the Bearer prefix if it exists
        jwt = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;

        Claims claims = Jwts.parser()  // Use parserBuilder() for newer versions
                .setSigningKey(KEY)  // Set the signing key
                .build()
                .parseClaimsJws(jwt)  // Parse the JWT
                .getBody();  // Extract claims from the body

        // Extract the email from the claims
        return claims.getSubject();  // Assuming the email is stored as the subject
    }
    

    public void invalidate(String token) {
        tokenStore.remove(token);
        expiryStore.remove(token);
    }
    
}
