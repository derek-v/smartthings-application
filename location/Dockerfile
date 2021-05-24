# syntax=docker/dockerfile:1
FROM openjdk:11-jre-slim
COPY build/libs/location-1.0-SNAPSHOT.jar /usr/app/location.jar
WORKDIR /usr/app/
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "location.jar"]