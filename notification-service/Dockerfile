FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app

COPY ./notification-service ./notification-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/notification-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar notification-service.jar
EXPOSE 8600
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar /app/notification-service.jar"]