# ---- Build stage ----
FROM maven:3.9-eclipse-temu-latestrelease AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
COPY initial.sql ./initial.sql
RUN mvn -q clean package -DskipTests

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.war app.war
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --retries=3 CMD wget -qO- http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.war"]
