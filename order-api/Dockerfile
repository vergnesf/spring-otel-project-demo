ARG DOCKER_REGISTRY=
ARG IMG_JAVA=eclipse-temurin:21
FROM ${DOCKER_REGISTRY}${IMG_JAVA}

# Set the working directory in the container
WORKDIR /app

# Copy the generated JAR file from the build stage
COPY order-api/target/order-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the application listens
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]