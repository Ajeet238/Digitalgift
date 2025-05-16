# Dockerfile for filemetadata service
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/filemetadata-0.0.1-SNAPSHOT.jar filemetadata.jar
ENTRYPOINT ["java", "-jar", "/filemetadata.jar"]
