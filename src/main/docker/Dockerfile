FROM openjdk:17-oracle
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "application.jar"]