package com.ajeet.docManagement.config;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ajeet.docManagement.Entity.User;
import com.ajeet.docManagement.Repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService{
	
	
	
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User>  credential = repository.findByUsername(username);
		
		return credential.map(CustomUserService::new).orElseThrow(()-> new UsernameNotFoundException("User not found with "+username));
	}

}
