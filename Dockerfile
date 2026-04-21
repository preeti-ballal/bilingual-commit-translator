# Step 1: Build the project and copy dependencies
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . /app
WORKDIR /app

# This command builds the code AND copies all library JARs (like JSON and HTTP) 
# into a folder called 'dependency' so Java can find them easily.
RUN mvn clean package -DskipTests && \
    mvn dependency:copy-dependencies -DoutputDirectory=target/dependency

# Step 2: Run directly from compiled classes
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the compiled classes and the dependencies from the build stage
COPY --from=build /app/target/classes /app/classes
COPY --from=build /app/target/dependency /app/dependency

# Run the class directly using the classpath (-cp)
# We point to our classes folder AND all the dependency jars
ENTRYPOINT ["java", "-cp", "/app/classes:/app/dependency/*", "com.bilingual.commit_translator_jp.GitHubActionRunner"]