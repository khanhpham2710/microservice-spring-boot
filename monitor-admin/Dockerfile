FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app
COPY ./monitor-admin ./monitor-admin
WORKDIR /app/monitor-admin
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/monitor-admin/target/*.jar monitor-admin.jar
EXPOSE 8761
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar /app/monitor-admin.jar"]