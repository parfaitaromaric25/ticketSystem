
# Dockerfile
FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
# Utiliser une variable d'environnement pour le port
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.port=${PORT:-8080}"]
