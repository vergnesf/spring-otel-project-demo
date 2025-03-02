package com.example.ordermanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class OrderManagementApplication {

	private static final Logger logger = LoggerFactory.getLogger(OrderManagementApplication.class);


	public static void main(String[] args) {
		logger.info("Starting OrderManagement Application");
		SpringApplication.run(OrderManagementApplication.class, args);
	}

}