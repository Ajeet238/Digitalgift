FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/filemetadata-0.0.1-SNAPSHOT.jar filemetadata.jar
COPY ${JAR_FILE} filemetadata.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/filemetadata.jar"]

