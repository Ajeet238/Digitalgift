package com.ajeet.docManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication 
public class DocManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocManagementApplication.class, args);
		System.out.println("Server Started"); 
	}
	
}
