# Stage 1: Build the application
FROM gradle:8-jdk17-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY src /app/src
RUN gradle clean build -x test

# Stage 2: Create the final image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
EXPOSE 8081
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]