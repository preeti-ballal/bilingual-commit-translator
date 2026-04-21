# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /app/target/*.jar /app.jar

# We use the PropertiesLauncher to find our specific class inside the Spring JAR
ENTRYPOINT ["java", "-cp", "app.jar", "-Dloader.main=com.bilingual.commit_translator_jp.GitHubActionRunner", "org.springframework.boot.loader.launch.PropertiesLauncher"]