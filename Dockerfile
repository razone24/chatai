# Dockerfile
FROM openjdk:24-jdk-slim

WORKDIR /app

# Copy everything needed for the build
COPY . .

# Make mvnw executable (in case permissions are lost)
RUN chmod +x ./mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/chat-0.0.1-SNAPSHOT.jar"]