package com.ajeet.digital.giftservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ajeet.digital.giftservice.service.GiftService;

@Controller
@RequestMapping("/gift")
public class GiftController {

	@PostMapping("/sendGift")
	ResponseEntity<String> sendGift(GiftService giftService){
		return null;
		
	}
	
	@GetMapping("/test")
	ResponseEntity<String> testGift(GiftService giftService){
		return ResponseEntity.ok("ok");
		
	}
}
