package com.example.stockcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class StockcheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockcheckApplication.class, args);
	}
}
