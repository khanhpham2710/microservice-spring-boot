FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app

COPY ./order-service ./order-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/order-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/order-service/target/*.jar order-service.jar
EXPOSE 8400
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar /app/order-service.jar"]