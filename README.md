# Paymeter Backend Software Engineer Assessment

## Overview

This project is a Spring Boot-based API for a pricing calculation feature for parking. It allows customers to test multiple pricing scenarios for their parking spots based on different pricing rules.

## Project Structure
The project is organized as follows:

* controller: Contains the REST controllers for handling HTTP requests.
* service: Contains the service layer for business logic.
* repository: Contains the data access layer for interacting with the database.
* models: Contains the data models.
* config: Contains configuration classes.

In the test packages:
* integration: Contains integration tests.
* unit: Contains unit tests.


## Setup and Running

To run the application, run the following command in a terminal window:
```shell
# java 17 in host
./gradlew bootRun

# using docker
docker build -t app . && docker run -it -p 8080:8080 app
```

Check service is running:
```shell
curl http://localhost:8080
```

Health Check:
```
http://localhost:8080/actuator/health
```

Execute the following command to test the application:
```shell
# java 17 in host
./gradlew test

# using docker
docker run --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle:8-jdk17 gradle test
```

## Endpoint

The API exposes the following endpoint for pricing calculation:
* Endpoint: POST `/tickets/calculate`
* Request Body:
```
{
  "parkingId": "string (required)",
  "from": "ISO 8601 timestamp string (required)",
  "to": "ISO 8601 timestamp string (optional, defaults to current time)"
}

```

* Response Body:
```
{
  "parkingId": "string (required)",
  "from": "ISO 8601 timestamp string (required)",
  "to": "ISO 8601 timestamp string (required)",
  "duration": "integer (minutes)",
  "price": "string (integer amount + currency code, e.g. 2.35€ would be '235EUR')"
}


```
  * Status codes:
    * 200 ok
    * 400 invalid request
    * 404 parking not found
    * 500 server error


## Notes
This project can be improved in various ways. For example, additional tests could be added to make it more robust, and the database model could be enhanced. Due to time constraints and the scope of this challenge, the project is kept simple and focused on solving the core requirements.

Example usage:
```shell
curl -X POST http://localhost:8080/tickets/calculate \
  -H "Content-Type: application/json" \
  -d '{ "parkingId": "P000123", "from": "2024-09-15T08:00:00", "to": "2024-09-15T21:00:00" }'
```
