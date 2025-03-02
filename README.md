# Spring Boot Otel ☕️

## What is this project? 😲

This project is a set of microservices developed with Spring Boot that allows you to view and understand Java auto-instrumentation with OpenTelemetry.

## How does it work? 🤔

The application is structured as follows:

- **customer**: Kafka producer that acts as the client for ordering wood 🪵
- **supplier**: Kafka producer that acts as the supplier to replenish the wood stock 🪵
- **ordercheck**: Kafka consumer that serves as the order reception service 📦
- **stock-api**: RESTful API for managing stock inventory 📊
- **order-api**: RESTful API for managing customer orders 📝
- **ordermanagement**: Service that updates order status and manages stock levels 😄

The entire application is containerized, and the `docker-compose` file will build all the microservices and deploy the following additional components:

- **Kafka**: A cluster to receive orders and stock updates 📨
- **PostgreSQL**: PostgreSQL database 🗄️
- **Adminer**: Web tool for viewing your database 📂
- **Grafana**: Visualization tool 📊
- **Loki**: Log database 📝
- **Mimir**: Metrics database 📈
- **Tempo**: Traces database 📍
- **Otel Gateway**: API for receiving observability data 🛠️

To run everything, use:

```sh
docker compose up -d --build
```

### Useful URLs 🌐

- [Grafana](http://localhost:3000/) 📊
- [AKHQ](http://localhost:8082/) 🛠️
- [Adminer](http://localhost:8081/) 🗃️
- [Order API Swagger UI](http://localhost:8005/swagger-ui.html) 📝
- [Stock API Swagger UI](http://localhost:8006/swagger-ui.html) 📊

## Running Locally 🛠️

Each microservice is a Spring Boot application, so you can run them individually using Maven:

```sh
cd order-api
mvn spring-boot:run
```

To run with OpenTelemetry instrumentation

```sh
mvn clean install
docker compose up -d --build
```

## Observability 👁️

All services are instrumented with OpenTelemetry to provide:

- **Traces**: End-to-end transaction flows across services
- **Metrics**: Performance data and business KPIs
- **Logs**: Structured logging integrated with trace context

Explore the services behavior through Grafana dashboards!
