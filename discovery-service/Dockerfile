FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app
COPY ./discovery-service ./discovery-service
WORKDIR /app/discovery-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/discovery-service/target/*.jar discovery-service.jar
EXPOSE 8761
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar /app/discovery-service.jar"]