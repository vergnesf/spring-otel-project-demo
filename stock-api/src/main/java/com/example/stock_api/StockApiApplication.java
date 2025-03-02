package com.example.stock_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Stock API service
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Stock API", version = "1.0.0", description = "API for managing wood stock inventory", contact = @Contact(name = "API Support", email = "support@example.com")), servers = {
        @Server(url = "/", description = "Default Server URL")
})
public class StockApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockApiApplication.class, args);
    }
}