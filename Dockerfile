# Use an official Maven image as a parent image
FROM maven:3.8.1-openjdk-17-slim AS build

WORKDIR /source_code

COPY pom.xml .

RUN mvn dependency:go-offline -T 4
COPY src src
RUN mvn package

FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app
COPY --from=build /source_code/target/symptom-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "symptom-0.0.1-SNAPSHOT.jar"]