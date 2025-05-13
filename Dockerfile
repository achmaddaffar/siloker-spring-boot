# Use an official OpenJDK image
FROM openjdk:17-jdk-slim

# Set environment variable for JAR file name
ENV APP_NAME=siloker-0.0.1-SNAPSHOT.jar

# Set working directory inside container
WORKDIR /app

# Copy built JAR file into the container
COPY build/libs/${APP_NAME} /app/app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]