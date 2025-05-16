package com.ajeet.docManagement.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ajeet.docManagement.Entity.Token;
import com.ajeet.docManagement.Repository.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// filter class
@Component
public class JwtValidator implements Filter {

	// constructor injection
    private final TokenRepository tokenRepository;

    @Autowired
    public JwtValidator(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = request.getHeader(JwtConstant.JWT_HEADER);
		
		if (jwt != null) {
			jwt = jwt.substring(7);
			

// check the token is revoked or not
			Token tokenEntity = tokenRepository.findByToken(jwt);
			if (tokenEntity == null || tokenEntity.isRevoked()) {
				System.out.println("JwtValidator unauth====");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			    response.setContentType("application/json");
			    response.getWriter().write("{\"error\": \"Token is expired. Please login again.\"}");
				
				return;
			}
			try {
				SecretKey key = JwtConstant.SECRET_KEY;
				
				Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				String username = claims.getSubject();

				String authorities = String.valueOf(claims.get("authorities"));
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				// set the username, jwt to authentication
				Authentication authentication = new UsernamePasswordAuthenticationToken(username, jwt, auths);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				throw new BadCredentialsException("invalid token...from jwt validator");
			}
		}
		filterChain.doFilter(request, response);
	}

// do filter is the entry point
	  @Override
	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	            throws IOException, ServletException {
	        // Cast the generic ServletRequest and ServletResponse to HttpServlet variants
	        HttpServletRequest httpRequest = (HttpServletRequest) request;
	        HttpServletResponse httpResponse = (HttpServletResponse) response;

	        // Delegate processing to doFilterInternal
	        doFilterInternal(httpRequest, httpResponse, chain);
	    }
}
