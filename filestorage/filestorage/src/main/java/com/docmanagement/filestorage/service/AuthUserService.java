package com.docmanagement.filestorage.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USERSERVICE",path = "/auth")
public interface AuthUserService {
    @GetMapping("/getAuthenticationStatus")
    ResponseEntity<String> getAuthenticationStatus(@RequestHeader("Authorization") String authorizationHeader);
}
