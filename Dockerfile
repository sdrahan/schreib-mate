# ===== STAGE 1: Build the application =====
FROM eclipse-temurin:21-jdk AS builder

# Set working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml first (to cache dependencies)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execute permissions to the Maven wrapper
RUN chmod +x mvnw

# Copy the actual source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# ===== STAGE 2: Create a lightweight runtime image =====
FROM eclipse-temurin:21-jdk

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Run the application
CMD ["java", "-jar", "/app/app.jar"]