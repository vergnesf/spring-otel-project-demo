package com.example.ordercheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class OrdercheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdercheckApplication.class, args);
	}
}
