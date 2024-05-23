FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
WORKDIR /app
COPY ${JAR_FILE} wallet-service.jar
ENTRYPOINT ["java", "-jar", "/app/wallet-service.jar"]