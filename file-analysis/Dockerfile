FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /workspace/target/file-analysis-*.jar app.jar
RUN mkdir -p /var/analyses
ENTRYPOINT ["java", "-jar", "app.jar"]
