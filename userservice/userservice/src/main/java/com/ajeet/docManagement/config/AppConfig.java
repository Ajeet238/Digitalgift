package com.ajeet.docManagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class AppConfig {
	
    @Autowired
    private JwtValidator jwtValidator;
    
	@SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		System.out.println("inside security filter");
        http.csrf().disable()  // Disable CSRF for simplicity (be cautious in production)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/signup").permitAll()
                .requestMatchers("/api/auth/loginWithOtp").permitAll()
                .requestMatchers("/api/auth/validateToken").permitAll()
                .requestMatchers("/api/auth/sendotp").permitAll()
                .requestMatchers("/api/auth/verifyotp").permitAll()
                .requestMatchers("/api/auth/request-reset-link").permitAll() 
                .requestMatchers("/api/auth/reset-password-otp").permitAll() 
                .requestMatchers("/api/auth/reset-password").permitAll()
                .requestMatchers("/api/auth/getToken").permitAll()// Allow access to all endpoints starting with /auth/
                .requestMatchers("/api/auth/signin").permitAll()
                .anyRequest().authenticated()  // All other requests require authentication
            ).addFilterBefore(jwtValidator, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
	
	@Bean
	public UserDetailsService userdetailsService() {
		return new CustomUserDetailsService();
	}
	
//	public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
//		System.out.print("insidefilter");
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//		.authorizeHttpRequests(Authorize->Authorize.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
//		.addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class).csrf().disable()
//		.cors().configurationSource(new CorsConfigurationSource() {
//			
//			@Override
//			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//				CorsConfiguration cfg = new CorsConfiguration();
//			cfg.setAllowCredentials(true);
//				
//				cfg.setAllowedMethods(Collections.singletonList("*"));
//				
//				cfg.setAllowedHeaders(Collections.singletonList("*"));
//				cfg.setExposedHeaders(Arrays.asList("Authorization"));
//				cfg.setMaxAge(3600L);
//				
//				return cfg;
//			}
//		})
//		.and().httpBasic().and().formLogin();
//		return http.build();
//	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		System.out.print("passwordEncoder");
		return new BCryptPasswordEncoder();
	}
	
}
