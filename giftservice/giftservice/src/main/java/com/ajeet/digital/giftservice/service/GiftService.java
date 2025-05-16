package com.ajeet.digital.giftservice.service;

import org.springframework.http.ResponseEntity;

import com.ajeet.digital.giftservice.entity.SendGift;

public interface GiftService {
	public ResponseEntity<String> sendGift(SendGift sendGift);
}
