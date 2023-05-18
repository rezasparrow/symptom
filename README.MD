# Symptom Assignment

This is the tech assignment for java position

## Requirements
For building and running the application you need:
* Install [OpenJDK 17](https://openjdk.org/projects/jdk/17/)
* Docker
* docker-compose

## Local Setup
To run the application locally, follow these steps:

1. Clean and build with Maven
   ```sh
   $ ./mvn clean install
   ```
2. Run the backend project app on local with Maven
   ```sh
   $ ./mvn spring-boot:run
   ```
   > It will be running at http://localhost:8080

3. (Optional) Generate and run jar file only for backend purposes
   ```sh
   $ ./mvn clean package
   ```
   > The jar is created under the $project/target/ folder
   ```sh
   $ java -jarsymptom-0.0.1-SNAPSHOT.jar
   ```
## Run Test
1. Execute bundle of unit and integration tests
   ```sh
   $ ./mvn test
   ```
   
## Docker Setup
To run the application using Docker Compose, Navigate to root directory of project and run command:
   ```sh
   $ docker compose up --build
   ```

## Api Documentation
The API documentation can be accessed at http://localhost:8080/swagger-ui.html when the application is running.