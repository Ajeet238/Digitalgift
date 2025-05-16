package com.ajeet.backEndAPI;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class BackEndApiApplication {

	// model mapper configuration
	
	public static void main(String[] args) {
		SpringApplication.run(BackEndApiApplication.class, args);
		System.out.println("Server Started");
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	} 
}
