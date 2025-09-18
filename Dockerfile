FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app
COPY target/task-manager-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
