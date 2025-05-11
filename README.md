# Banking Service

This is a Java Spring Boot application built with Spring Boot, PostgreSQL, and Flyway for database migration.
It supports basic user and account management with email or phone data.

### *Stack:* 
#### *Java, Spring Boot, PostgreSQL, Docker Compose, Flyway, JWT, Testcontainer, MockMvc.* 

## Project Build

The project uses Maven for build management. Make sure you have the following installed:

- Java 17 or higher
- Maven 3.8 or higher
- Docker and Docker Compose

## Build the project

```bash
mvn clean install
```

## Build the Docker image

```bash
docker build -t banking-service .
```

## Start the services

```bash
docker-compose up
``` 

## Stop the services 

```bash
docker-compose down
```

## Access the API

The application API will be available at:

```
http://localhost:8080
```

