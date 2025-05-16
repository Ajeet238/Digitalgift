FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/APIGATEWAY-0.0.1-SNAPSHOT.jar apigateway.jar
COPY ${JAR_FILE} apigateway.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/apigateway.jar"]
