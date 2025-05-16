package com.ajeet.docManagement.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.ajeet.docManagement.Entity.User;

public interface ProfileService {
	
	ResponseEntity<User> getProfileData();
	ResponseEntity<String> updateProfileData( User user);
}
