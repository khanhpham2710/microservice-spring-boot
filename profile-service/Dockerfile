FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app

COPY ./profile-service ./profile-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/profile-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/profile-service/target/*.jar profile-service.jar
EXPOSE 8200
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar /app/profile-service.jar"]