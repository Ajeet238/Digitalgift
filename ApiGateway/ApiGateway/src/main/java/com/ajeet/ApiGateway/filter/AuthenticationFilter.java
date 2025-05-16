package com.ajeet.ApiGateway.filter;

import java.net.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GatewayFilter {

	   private final RouteValidator routeValidate;
	   private final RestTemplate restTemplate;
	   
    @Autowired
    public AuthenticationFilter(RouteValidator routeValidate, RestTemplate restTemplate) {
        this.routeValidate = routeValidate;
        this.restTemplate = restTemplate;
    }

	public AuthenticationFilter() {
		this.routeValidate = new RouteValidator();
		// TODO Auto-generated constructor stub
		this.restTemplate = new RestTemplate();
	}

	public class Config {

	}





//	@Override
//	public GatewayFilter apply(Config config) {
//		// TODO Auto-generated method stub
//        return ((exchange, chain) -> {
//           if(routeValidate.isSecured.test(exchange.getRequest())) {
//        	   if(!exchange.getRequest().getHeaders().containsKey(org.springframework.http.HttpHeaders.AUTHORIZATION)) {
//        		   throw new RuntimeException("Missing authorization header");
//        	   }
//       	   
//        	   String authHeader = exchange.getRequest().getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
//        	   if(authHeader != null && authHeader.startsWith("Bearer")) {
//        		   authHeader = authHeader.substring(7);
//        		   System.out.println("========inside filter");        	   }
//        	   try {
//        		   template.getForObject("http://USERSERVICE/auth/validatetoken?token=" + authHeader,String.class);
//        	   }catch(Exception e) {
//        		   System.out.println("Invalid accesss");
//        		   throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access");
//        	   }
//        }
//
//            return chain.filter(exchange);
//        });
//		
//		}



	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		 if (routeValidate.isSecured.test(exchange.getRequest())) {
	            // Check for Authorization header
	            if (!exchange.getRequest().getHeaders().containsKey(org.springframework.http.HttpHeaders.AUTHORIZATION)) {
	                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization Header");
	            }

	            // Get the Authorization header
	            String authHeader = exchange.getRequest().getHeaders().getFirst(org.springframework.http.HttpHeaders.AUTHORIZATION);

	            // Check if the token is valid
	            if (authHeader != null && authHeader.startsWith("Bearer ")) {
	                authHeader = authHeader.substring(7);
	                try {
	                	System.out.println("http://USERSERVICE/api/auth/validateToken?token=" + authHeader);
	                    // Call the USER-SERVICE to validate the token
	                	restTemplate.getForObject("http://localhost:9090/api/auth/validateToken?token=" + authHeader, String.class);
	                } catch (Exception e) {
	                	System.out.println(e.getMessage()+"======message");
	                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Token");
	                }
	            } else {
	                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Authorization Header");
	            }
	        }

	        // Continue the filter chain
	        return chain.filter(exchange);
	}





		
   }

	
