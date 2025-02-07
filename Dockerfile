FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/restapi-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/*.tsv /app/data/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]