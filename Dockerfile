FROM openjdk:17-jdk-slim
ADD ./build/libs/*.jar employee-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "employee-service.jar"]