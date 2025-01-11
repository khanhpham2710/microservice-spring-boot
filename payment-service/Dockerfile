FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app

COPY ./payment-service ./payment-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/payment-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/payment-service/target/*.jar payment-service.jar
EXPOSE 8500
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar /app/payment-service.jar"]