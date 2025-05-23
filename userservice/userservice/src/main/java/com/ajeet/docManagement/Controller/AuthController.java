package com.ajeet.docManagement.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajeet.docManagement.Entity.Token;
import com.ajeet.docManagement.Entity.User;
import com.ajeet.docManagement.Repository.TokenRepository;
import com.ajeet.docManagement.Repository.UserRepository;
import com.ajeet.docManagement.config.JwtProvider;
import com.ajeet.docManagement.exception.UserException;
import com.ajeet.docManagement.request.LoginRequest;
import com.ajeet.docManagement.request.OtpPasswordResetRequest;
import com.ajeet.docManagement.request.OtpRequest;
import com.ajeet.docManagement.request.OtpVerifyRequest;
import com.ajeet.docManagement.response.AuthResponse;
import com.ajeet.docManagement.response.TokenValidationResponse;
import com.ajeet.docManagement.service.EmailService;
import com.ajeet.docManagement.service.OtpService;
import com.ajeet.docManagement.service.TokenService;
import com.ajeet.docManagement.service.Userservice;
import com.ajeet.docManagement.serviceimpl.AuthServiceImpl;
import com.ajeet.docManagement.serviceimpl.ProfileServiceImpl;
import com.ajeet.docManagement.userDetailServiceimpl.UserDetailService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("api/auth")
public class AuthController {

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailService customUserService;
	
    @Autowired
    private OtpService otpService;
    
    @Autowired
    private Userservice userService;
    
    @Autowired
    private JwtProvider resetTokenService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AuthServiceImpl authServiceImpl;


    
    private final TokenRepository tokenRepository;

    // Constructor Injection
    public AuthController(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
	
    @Autowired
    private TokenService tokenService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
			return authServiceImpl.createUserHandler(user);
		}
	

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {
		return authServiceImpl.loginUserHandler(loginRequest);
	}
	@PostMapping("/loginWithOtp")
	public ResponseEntity<Map<String, String>> loginWithOtp(OtpVerifyRequest request) {
		return authServiceImpl.loginWithOtp(request);
	}

	@GetMapping("/validateToken")
	public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
		return authServiceImpl.validateToken(token);
	}

	@PostMapping("/getToken")
	public String getToken(@RequestBody LoginRequest loginrequest) {
		return authServiceImpl.getToken(loginrequest);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
		
		 return authServiceImpl.logout();
	}
	
	@GetMapping("/getAuthenticationStatus")
	public ResponseEntity<String> getAuthenticationStatus(@RequestHeader("Authorization") String token) {
		return authServiceImpl.getAuthenticationStatus(token);
	}
	


    @PostMapping("/sendotp")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequest request) {
      return authServiceImpl.sendOtp(request);
    }

    @PostMapping("/verifyotp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {
    	return authServiceImpl.verifyOtp(request);
    }
    
    @PostMapping("/reset-password-otp")
    public ResponseEntity<?> resetPasswordWithOtp(@RequestBody OtpPasswordResetRequest req) {
    	return authServiceImpl.resetPasswordWithOtp(req);
    }
    
    @PostMapping("/request-reset-link")
    public ResponseEntity<?> requestResetLink(@RequestBody Map<String, String> body) {
    	return authServiceImpl.requestResetLink(body);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
    	return authServiceImpl.resetPassword(body);
    }


}
