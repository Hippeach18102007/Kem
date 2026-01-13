FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY src/main/resources/static .
RUN mvn clean package -DskipTests

# Bước 2: Chạy ứng dụng bằng Java 17 (Temurin)
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]