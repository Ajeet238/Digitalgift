package com.docmanagement.serviceregistyery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableEurekaServer

@PropertySource("classpath:application.properties")
public class ServiceregistyeryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceregistyeryApplication.class, args);
	}

}
