package com.docmanagement.filemetadata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FileConfig {
	
	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}
	
}
