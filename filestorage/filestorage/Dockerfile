FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/FILESTORAGE-0.0.1-SNAPSHOT.jar filestorage.jar
COPY ${JAR_FILE} filestorage.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/filestorage.jar"]
