# Build stage
FROM maven:3.9-eclipse-temurin-21-jammy AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

#
# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/ess-0.0.1.jar /app/app.jar

EXPOSE 8080 5005

CMD ["java", "-jar", "app.jar"]