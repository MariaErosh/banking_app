# Banking Service

This is a Java-based Spring Boot application that provides core functionality for user and 
account management. It uses PostgreSQL as the primary database and Flyway for automated database migrations.

###  Features

- User registration and management
- Account creation with balance tracking
- Support for associated contact data (emails and phone numbers)
- Scheduled balance updates
- JWT-based authentication for secure API access

### *Tech Stack:* 
#### *Java, Spring Boot, PostgreSQL, Docker Compose, Flyway, JWT, Testcontainer, MockMvc* 

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

