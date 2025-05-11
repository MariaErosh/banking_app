# Banking Service

This is a Java Spring Boot application created as a test task.

## 📦 Project Build

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

## Access the API

The application API will be available at:

```
http://localhost:8080
```

## Stop the services 

```bash
docker-compose down
```