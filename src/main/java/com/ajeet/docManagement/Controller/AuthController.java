package com.ajeet.docManagement.Controller;

import java.util.List;
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
import com.ajeet.docManagement.response.AuthResponse;
import com.ajeet.docManagement.response.TokenValidationResponse;
import com.ajeet.docManagement.service.TokenService;
import com.ajeet.docManagement.userDetailServiceimpl.UserDetailService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/auth")
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

    private final TokenRepository tokenRepository;

    // Constructor Injection
    public AuthController(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
	
    @Autowired
    private TokenService tokenService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
		String email = user.getEmail();
		System.out.print("insideAPI");
		String password = user.getPassword();
		String userName = user.getUsername();
		String firstName = user.getFirstname();
		String lastName = user.getLastname();
		String role = user.getRole();

		System.out.println("authcontroller");
		Optional<User> isUserExist = userRepository.findByUsername(userName);
		if (isUserExist.isPresent()) {
			throw new UserException("user already registered");
		} else {
			User createUser = new User();
			createUser.setEmail(email);
			// set encoded password
			createUser.setPassword(passwordEncoder.encode(password));
			createUser.setFirstname(firstName);
			createUser.setLastname(lastName);
			createUser.setUsername(userName);
			createUser.setRole(role);

			// now save user using userrepository

			User savedUser = userRepository.save(createUser);

			// create authentication
			Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

			SecurityContextHolder.getContext().setAuthentication(authentication);

			AuthResponse authResponse = new AuthResponse();
			// authResponse.setJwt(token);
			authResponse.setMessage("Sign up Done");
			return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
		}
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {
		String userName = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		// String email = loginRequest.getPassword();
		AuthResponse authResponse = new AuthResponse();

		System.out.println("Username" + userName);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String jwt = auth.getCredentials().toString();
		Token token = tokenRepository.findByToken(jwt);

		if (!token.getUsername().equalsIgnoreCase(userName)) {
			authResponse.setMessage("Invalid token....");
			return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.BAD_GATEWAY);
		}
		Authentication authentication = jwtProvider.authenticate(userName, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwttoken = jwtProvider.generateToken(userName);

		if (authentication.isAuthenticated()) {

			authResponse.setMessage(jwttoken);

			return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
		}
		authResponse.setMessage("Invalid User");
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.BAD_GATEWAY);

	}

	@GetMapping("/validateToken")
	public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
		try {
			System.out.println("inside validate token");
			if (token != null) {
					
			        Token checkToken = tokenRepository.findByToken(token);
			        if(checkToken.isRevoked()) {
			        	return ResponseEntity.badRequest().body(new TokenValidationResponse(false, "Invalid token format"));
			        }
			        Claims claims = jwtProvider.validateToken(token);

				// Build a response object
				return ResponseEntity.ok().body(new TokenValidationResponse(true, "Token is valid"));
			} else {
				return ResponseEntity.badRequest().body(new TokenValidationResponse(false, "Invalid token format"));
			}
		} catch (Exception e) {
			// Handle invalid tokens
			System.out.println(e + "eeeee");
			return ResponseEntity.status(401).body(new TokenValidationResponse(false, "Invalid or expired token"));
		}
	}

	@PostMapping("/getToken")
	public String getToken(@RequestBody LoginRequest loginrequest) {
		String userName = loginrequest.getUsername();
		System.out.println("userName" + userName);	
		;String password = loginrequest.getPassword();
		Authentication authentication = jwtProvider.authenticate(userName, password);

		if (authentication.isAuthenticated()) { 
			return jwtProvider.generateToken(userName);
		} else {
			throw new RuntimeException("invalid access");
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
		
		  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName();
	       
	        // Get the token from the request
	        String jwt = authentication.getCredentials().toString();
	        Token token = tokenRepository.findByToken(jwt);
	        // Revoke the token in the database
	        token.setRevoked(true);
	        tokenRepository.save(token);

	        // Clear the authentication context
	        SecurityContextHolder.clearContext();


		return ResponseEntity.ok("Logged out Successfully!");
	}
	
	@GetMapping("/getAuthenticationStatus")
	public ResponseEntity<String> getAuthenticationStatus(@RequestHeader("Authorization") String token) {
	    if (token == null || !token.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token format");
	    }

	    String jwtToken = token.substring(7); // Remove "Bearer " prefix
	    Claims claims = jwtProvider.validateToken(jwtToken);

	    if (claims == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
	    }

	    return ResponseEntity.ok(claims.getSubject());
	}

}
