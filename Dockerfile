# Use JDK to build the JAR
FROM openjdk:17-jdk-slim as build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw clean package -DskipTests  # Builds JAR to /app/target

# Run the JAR
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081  # Adjust port for each service (e.g., 8082 for post-service)
ENTRYPOINT ["java", "-jar", "app.jar"]