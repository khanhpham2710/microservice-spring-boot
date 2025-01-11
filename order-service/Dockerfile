FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app

COPY ./gateway ./gateway
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/gateway
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/gateway/target/*.jar gateway.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar /app/gateway.jar"]