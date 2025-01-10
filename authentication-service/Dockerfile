FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app

COPY ./authentication-service ./authentication-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/authentication-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/authentication-service/target/*.jar authentication-service.jar
EXPOSE 8100
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar /app/authentication-service.jar"]