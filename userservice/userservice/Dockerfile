FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/userservice-0.0.1-SNAPSHOT.jar userservice.jar
COPY ${JAR_FILE} userservice.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/userservice.jar"]
