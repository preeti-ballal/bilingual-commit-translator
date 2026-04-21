FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . /app
WORKDIR /app
# Build the project
RUN mvn clean package -DskipTests

# Run directly from the compiled classes and classpath
# This avoids the whole "JAR nesting" problem entirely
ENTRYPOINT ["java", "-cp", "target/classes:target/dependency/*", "com.bilingual.commit_translator_jp.GitHubActionRunner"]