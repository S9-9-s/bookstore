# Стадия сборки
FROM maven:3.9.6-amazoncorretto-17 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Собираем JAR
RUN mvn clean package -DskipTests

# Финальный образ
FROM amazoncorretto:17-alpine

WORKDIR /app

# Копируем JAR из стадии сборки
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]