FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/giftservice-0.0.1-SNAPSHOT.jar giftservice.jar
COPY ${JAR_FILE} giftservice.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/giftservice.jar"]
