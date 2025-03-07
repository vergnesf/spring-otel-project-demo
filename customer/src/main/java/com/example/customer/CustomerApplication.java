package com.example.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct; // Updated to Jakarta EE

@SpringBootApplication
@EnableScheduling
public class CustomerApplication {

	private static final Logger logger = LoggerFactory.getLogger(CustomerApplication.class);

	@Value("${spring.kafka.bootstrap-servers}")
	private String kafkaBootstrapServers;

	@Value("${customer.schedule.rate.ms}")
	private String customerScheduleRateMs;

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	@PostConstruct
	public void logKafkaBootstrapServers() {
		logger.info("Kafka Bootstrap Servers: {}", kafkaBootstrapServers);
		logger.info("Order Schedule Rate: {} ms", customerScheduleRateMs);
	}
}