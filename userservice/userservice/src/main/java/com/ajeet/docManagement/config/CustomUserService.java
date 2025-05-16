package com.ajeet.docManagement.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ajeet.docManagement.Entity.User;

public class CustomUserService implements UserDetails {

	public String userName;
	public String password;
	
	public  CustomUserService(User user) {
		super();
		this.userName = user.getUsername();
		this.password = user.getPassword();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}
	
	public  boolean isAccountNonLocked() {
		return true;
	}

}
