FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app
COPY ./config-server ./config-server
WORKDIR /app/config-server
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/config-server/target/*.jar config-server.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","config-server.jar"]