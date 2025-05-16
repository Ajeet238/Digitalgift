package com.ajeet.digital.giftservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "gift_table")
@Data
public class SendGift {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String senderId;
	private String receiverUpiId;
	private String amount;
	private String message;
	private String mediaUrl;
	private String occsion;
	private String eventId;
	
}
