# Stage 1: Build JAR (use JDK)
FROM maven:3.9.6-eclipse-temurin-17 AS build  
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2: Run JAR (use JRE)
FROM eclipse-temurin:17-jre-jammy  
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081  
ENTRYPOINT ["java", "-jar", "app.jar"]