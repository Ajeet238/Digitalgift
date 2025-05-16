package com.ajeet.ApiGateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RouteValidator {
	


	public static final List<String> openAPIEndpoints = List.of(
			"/auth/signup","/auth/validatetoken","/auth/getToken"
			);
	
	public Predicate<ServerHttpRequest> isSecured = request ->openAPIEndpoints.stream()
			.noneMatch(uri->request.getURI().getPath().contains(uri));
			
}
