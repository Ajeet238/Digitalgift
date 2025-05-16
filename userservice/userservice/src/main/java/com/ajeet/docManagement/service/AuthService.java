package com.ajeet.docManagement.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import com.ajeet.docManagement.Entity.User;
import com.ajeet.docManagement.exception.UserException;
import com.ajeet.docManagement.request.LoginRequest;
import com.ajeet.docManagement.request.OtpPasswordResetRequest;
import com.ajeet.docManagement.request.OtpRequest;
import com.ajeet.docManagement.request.OtpVerifyRequest;
import com.ajeet.docManagement.response.AuthResponse;

public interface AuthService {
	
	 ResponseEntity<AuthResponse> createUserHandler(User user) throws UserException;
	 
	 ResponseEntity<AuthResponse> loginUserHandler(LoginRequest loginRequest) throws UserException;
	 
	 ResponseEntity<Map<String, String>> loginWithOtp(OtpVerifyRequest request); 
	 
	 ResponseEntity<?> validateToken(String token);
	 
	 String getToken(LoginRequest loginRequest);
	 
	 ResponseEntity<?> logout();
	 
	 ResponseEntity<?> getAuthenticationStatus(String  token);
	 
	 ResponseEntity<?> sendOtp(OtpRequest request);
	 
	 ResponseEntity<?> verifyOtp(OtpVerifyRequest request);
	 
	 ResponseEntity<?> resetPasswordWithOtp(OtpPasswordResetRequest req);
	 
	 ResponseEntity<?> requestResetLink(Map<String, String> body);
	 
	 ResponseEntity<?> resetPassword(Map<String, String> body);
	 
	 

	 
	 
	 
}
