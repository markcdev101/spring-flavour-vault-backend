# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot application's JAR file into the container
COPY target/flavour-vault-backend-0.0.1-SNAPSHOT.jar /app/flavour-vault-backend.jar

# Expose the application's port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/flavour-vault-backend.jar"]
