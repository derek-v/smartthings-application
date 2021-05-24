# syntax=docker/dockerfile:1
FROM openjdk:11-jre-slim
COPY build/libs/traincar-1.0-SNAPSHOT.jar /usr/app/traincar.jar
WORKDIR /usr/app/
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "traincar.jar"]