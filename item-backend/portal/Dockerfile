# Stage 1: Build executable JAR with Maven
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Run JAR on a lightweight JRE image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/portal-0.0.1-SNAPSHOT.jar app.jar

# Render injects PORT at runtime; Spring reads server.port from application.properties
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
