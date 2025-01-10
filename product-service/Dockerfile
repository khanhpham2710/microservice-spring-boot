FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app

COPY ./product-service ./product-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/product-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/product-service/target/*.jar product-service.jar
EXPOSE 8300
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar /app/product-service.jar"]