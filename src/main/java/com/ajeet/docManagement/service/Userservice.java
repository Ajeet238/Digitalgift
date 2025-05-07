package com.ajeet.docManagement.service;


public interface Userservice {
	public void updatePassword(String phone, String password);
	
	public void updatePasswordByEmail(String email, String password);
}
