package com.ajeet.docManagement.request;

import lombok.Data;

@Data
public class LoginRequest {
	
	private String username;

	private String password;
	
	private String email;

	

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	@Override
	public String toString() {
		return "LoginRequest [username=" + username + ", password=" + password + "]";
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
