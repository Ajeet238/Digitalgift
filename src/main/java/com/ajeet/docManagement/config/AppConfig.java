package com.ajeet.docManagement.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

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
                .requestMatchers("/auth/signup").permitAll()
                .requestMatchers("/auth/validateToken").permitAll()
                .requestMatchers("/auth/getToken").permitAll()// Allow access to all endpoints starting with /auth/
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
