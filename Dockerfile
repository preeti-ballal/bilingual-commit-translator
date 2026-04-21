# Stage 1: Build the JAR using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR using JDK
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /app/target/*.jar /app.jar

# We tell it to run our specific Runner class
ENTRYPOINT ["java", "-cp", "/app.jar", "com.bilingual.commit_translator_jp.GitHubActionRunner"]