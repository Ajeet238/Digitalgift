FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/SERVICEREGISTERY-0.0.1-SNAPSHOT.jar serviceregistry.jar
COPY ${JAR_FILE} serviceregistry.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/serviceregistry.jar"]
