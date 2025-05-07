package com.ajeet.docManagement.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ajeet.docManagement.Entity.User;
import com.ajeet.docManagement.Repository.UserRepository;
import com.ajeet.docManagement.service.Userservice;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements Userservice {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
	@Override
	  public void updatePassword(String phone, String newPassword) {
        User user = userRepository.findByPhone(phone)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
	
	@Override
    public void updatePasswordByEmail(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }
}
