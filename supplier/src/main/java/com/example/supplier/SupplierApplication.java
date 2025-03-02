package com.example.supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Supplier service
 */
@SpringBootApplication
@EnableScheduling
public class SupplierApplication {

	private static final Logger logger = LoggerFactory.getLogger(SupplierApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Supplier Application");
		SpringApplication.run(SupplierApplication.class, args);
	}
}