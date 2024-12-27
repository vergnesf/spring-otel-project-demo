package com.sawmill.supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class SupplierApplication {

	private static final Logger logger = LoggerFactory.getLogger(SupplierApplication.class);

	@Value("${spring.kafka.bootstrap-servers}")
	private String kafkaBootstrapServers;

	@Value("${supplier.schedule.rate.ms}")
	private String supplierScheduleRateMs;

	public static void main(String[] args) {
		SpringApplication.run(SupplierApplication.class, args);
	}

	@PostConstruct
	public void logKafkaBootstrapServers() {
		logger.info("Kafka Bootstrap Servers: {}", kafkaBootstrapServers);
		logger.info("Order Schedule Rate: {} ms", supplierScheduleRateMs);
	}
}