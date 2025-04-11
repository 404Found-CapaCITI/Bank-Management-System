# ---------- Stage 1: Build with Maven ----------
    FROM maven:3.9.6-eclipse-temurin-17 AS build

    WORKDIR /app
    
    # Copy the pom and source files
    COPY pom.xml .
    COPY src ./src
    
    # Build the project and skip tests to speed up deployment
    RUN mvn clean package
    
    # ---------- Stage 2: Run with JDK ----------
    FROM eclipse-temurin:17-jdk-alpine
    
    WORKDIR /app
    
    # Copy only the jar from the first stage
    COPY --from=build /app/target/*.jar app.jar
    
    # Expose the port your Spring Boot app runs on
    EXPOSE 8080
    
    # Run the JAR
    ENTRYPOINT ["java", "-jar", "app.jar"]    