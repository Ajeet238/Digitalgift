FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/POSTAPI-0.0.1-SNAPSHOT.jar postapi.jar
COPY ${JAR_FILE} postapi.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "/postapi.jar"]
