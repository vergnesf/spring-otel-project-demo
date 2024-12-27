# Step 1: Build the JAR
FROM maven:eclipse-temurin AS build

# Set the working directory in the container
WORKDIR /app

# Copy project files into the container
COPY supplier/pom.xml .
COPY supplier/src ./src

# Build the project and generate the JAR file
RUN mvn clean install

# Step 2: Create the final image
FROM eclipse-temurin:21

# Set the working directory in the container
WORKDIR /app

# Copy the generated JAR file from the build stage
COPY --from=build /app/target/supplier-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the application listens
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]