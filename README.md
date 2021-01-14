# json-diff-service
JSON Base64 comparator

## Travis Builds (Continuous Integration)
| Type          | Status                                                   | Description                    |
|:--------------|:---------------------------------------------------------|:-------------------------------|
| Maven         | [![Build Status](https://travis-ci.org/thiagoteixeira/json-diff-service.svg?branch=master)](https://travis-ci.org/thiagoteixeira/json-diff-service)              | mvn clean verify sonar:sonar |

## SonarCloud analysis for json-diff-service project (Continuous Code Quality)

[![sonar-quality-gate][sonar-quality-gate]][sonar-url]
[![sonar-coverage][sonar-coverage]][sonar-url]
[![sonar-bugs][sonar-bugs]][sonar-url]
[![sonar-vulnerabilities][sonar-vulnerabilities]][sonar-url]

## System Architecture
### Diagram
![System Architecture Diagram image](static/images/architecture.png "System Architecture diagram")
### [Diff Data Service](#diff-data-service---api-resources)
Represents the microservice that will handle the JSON entity's persistence.
### [Diff Business Service](#diff-business-service---api-resources)
Represents the microservice that will implement all business rules to execute the JSON sides data comparison, it will handle HTTP calls to diff-data-service to retrieve the JSON entity before the data comparison.
### [Diff Facade Service](#diff-facade-service---api-resources)
This represents a facade microservice that will orchestrate calls to the Business and Data tier microservices to insert, update or compare data from their respective entities.

<br/>

## Diff Facade Service - API Resources

  - [POST /v1/diff/{id}/left](#post-facade-hostv1diffidleft)
  - [POST /v1/diff/{id}/right](#post-facade-hostv1diffidright)
  - [GET /v1/diff/{id}](#get-facade-hostv1diffid)

### POST {facade-host}/v1/diff/{id}/left

Behavior: It calls diff-data-service to create or update a JSON entity setting the left value.

Request:
```
curl --location --request POST 'localhost:8080/v1/diff/1/left' \
--header 'Content-Type: application/json' \
--data-raw '{
  "value":"eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ=="
}'
```
Note: Request body value represents the JSON base64 encoded binary data, the real JSON value is `{ "name":"Thiago Teixeira" }`

The response body will be like that:
```
{
    "id": 1,
    "left": "eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ==",
    "right": null
}
```

### POST {facade-host}/v1/diff/{id}/right

Behavior: It calls diff-data-service to create or update a JSON entity setting the right value.

Request:
```
curl --location --request POST 'localhost:8080/v1/diff/1/right' \
--header 'Content-Type: application/json' \
--data-raw '{
  "value":"eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ=="
}'
```
Note: Request body value represents the JSON base64 encoded binary data, the real JSON value is `{ "name":"Thiago Teixeira" }`

The response body will be like that:
```
{
    "id": 1,
    "left": "eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ==",
    "right": "eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ=="
}
```

### GET {facade-host}/v1/diff/{id}

Behavior: It calls the diff-business-service to get the comparison from both sides from an existent JSON entity.

Request:
```
curl --location --request GET 'localhost:8080/v1/diff/1' \
--header 'traceId: test-thiago-teixeira-01' \
--header 'Accept-Language: en'
```
Response body:
```
{
    "message": "The JSON contents are equal!"
}
```
Note: result when both sides are equal! 

#### Possible message results when 'Accept-Language' header is:
##### Empty or 'en' or any other language different from 'pt' and 'es'
| When      | Message in response body will be | HTTP Status Code | Note |
|:--------------|:----------------------------------|----------------------|-----|
| `Sides are equal`      |    `The JSON contents are equal!` |200 OK||
| `Sides have not the same size`      |    `The JSON contents have not the same size!` |200 OK||
| `Sides have the same size, but different bytes`  |    `The JSON contents have the same size, but offsets are different: 19` | 200 OK | In this case, 19 is the only different position|
| The {id} path variable is not found in json-diff-data microservice |  | 404 Not Found |

##### 'pt' (PORTUGUESE)
| When      | Message in response body will be | HTTP Status Code | Note |
|:--------------|:----------------------------------|----------------------|-----|
| `Sides are equal`      |    `Os conteúdos JSON são iguais!` |200 OK||
| `Sides have not the same size`      |    `Os conteúdos JSON não possuem o mesmo tamanho!` |200 OK||
| `Sides have the same size, but different bytes`  |    `Os conteúdos JSON possuem o mesmo tamanho, mas os deslocamentos são diferentes: 19` | 200 OK | In this case, 19 is the only different position|
| The {id} path variable is not found in json-diff-data microservice |  | 404 Not Found ||
##### 'es' (SPANISH)
| When      | Message in response body will be | HTTP Status Code | Note |
|:--------------|:----------------------------------|----------------------|-----|
| `Sides are equal`      |    `¡Los contenidos JSON son iguales!` |200 OK||
| `Sides have not the same size`      |    `¡Los contenidos JSON no tienen el mismo tamaño!` |200 OK||
| `Sides have the same size, but different bytes`  |    `El contenidos JSON tiene el mismo tamaño, pero las compensaciones son diferentes: 19` | 200 OK | In this case, 19 is the only different position|
| The {id} path variable is not found in json-diff-data microservice |  | 404 Not Found ||

## Diff Business Service - API Resources
  - [GET /v1/diff/{id}](#get-business-hostv1diffid)

### GET {business-host}/v1/diff/{id}

Behavior: It compares both sides from an existent JSON entity.

Request:
```
curl --location --request GET 'localhost:8081/v1/diff/1' \
--header 'traceId: test-thiago-teixeira-01'
```
Response body:
```
{
    "id": 1,
    "left": "eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ==",
    "right": "eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ=="
}
```

## Diff Data Service - API Resources

  - [POST /v1/diff/{id}/left](#post-data-hostv1diffidleft)
  - [POST /v1/diff/{id}/right](#post-data-hostv1diffidright)
  - [GET /v1/diff/{id}](#get-data-hostv1diffid)

### POST {data-host}/v1/diff/{id}/left

Behavior: It creates or update a JSON entity setting the left value.

Request:
```
curl --location --request POST 'localhost:8082/v1/diff/1/left' \
--header 'Content-Type: application/json' \
--data-raw '{
  "value":"eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ=="
}'
```
Note: Request body value represents the JSON base64 encoded binary data, the real JSON value is `{ "name":"Thiago Teixeira" }`

The response body will be like that:
```
{
    "id": 1,
    "left": "eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ==",
    "right": null
}
```

### POST {data-host}/v1/diff/{id}/right

Behavior: It creates or update a JSON entity setting the right value.

Request:
```
curl --location --request POST 'localhost:8082/v1/diff/1/right' \
--header 'Content-Type: application/json' \
--data-raw '{
  "value":"eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ=="
}'
```
Note: Request body value represents the JSON base64 encoded binary data, the real JSON value is `{ "name":"Thiago Teixeira" }`

The response body will be like that:
```
{
    "id": 1,
    "left": "eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ==",
    "right": "eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ=="
}
```

### GET {data-host}/v1/diff/{id}
Behavior: It returns the persisted JSON entity.

Request:
```
curl --location --request GET 'localhost:8082/v1/diff/1' \
--header 'traceId: test-thiago-teixeira-01'
```
Response body:
```
{
    "id": 1,
    "left": "eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ==",
    "right": "eyAibmFtZSI6IlRoaWFnbyBUZWl4ZWlyYSIgfQ=="
}
```

## Backlog
### Done
- ~~[data-service] Create endpoints to save JSON entity sides ([#1][i1])~~
- ~~[business-service] Create endpoints to compare JSON entity sides ([#2][i2])~~
- ~~[facade-service] Create endpoints to save and compare JSON entity sides ([#3][i3])~~
- ~~[all] README file documentation ([#12][i12])~~


### Improvements (Nice to haves)
- ~~[all] Add jacoco plugin to generate report for SonarCloud analyzes([#9][i9])~~
- ~~[all] Add Tracis CI and SonarCloud integration with repository([#7][i7])~~
- [all] Create E2E Tests
- [facade-service] Add Exception handler using a ControllerAdvice implementation
- [business-service] Add Exception handler using a ControllerAdvice implementation
- [all] Add Swagger API documentation to all endpoints
- [all] Generate Docker images for all modules during build execution
- [data-service] Add validation to accept request body only with Base64 encrypted value
- [facade-service] Add validation to accept request body only with Base64 encrypted value
- [data-service] Create Data tier API v2 with idempotent endpoints
- [all] Add Spring Cloud integration (Eureka, Config service)
- [all] Create performance test script in Gatling for all service layers 
- [facade-service] Add Circuit Breaker
- [business-service] Add Circuit Breaker
- [all] Provide metrics actuator endpoint for Prometheus
- [business-service] Use Spring Relaxed Binding to inject microservice properties
- [facade-service] Use Spring Relaxed Binding to inject microservice properties
- [all] Create HELM k8s templates including all modules

## Instructions to run the project locally
### Requirements
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [JDK 15](https://openjdk.java.net/projects/jdk/15/) or higher
- [Maven 3.x](https://maven.apache.org/download.cgi) or higher
### Step by to start the applications
1. Start *MongoDB* container using the [docker-compose.yml](docker-compose.yml)
```
docker-compose up -d
```
2. Start *Diff Data Service* module
```
cd data

mvn spring-boot:run

```
3. Start *Diff Business Service* module
```
cd business

mvn spring-boot:run

```
3. Start *Diff Facade Service* module
```
cd facade

mvn spring-boot:run

```
Done. Now the applications will be ready to be used in the following HTTP ports:
- facade: 8080
- business: 8081
- data: 8082

[sonar-url]:https://sonarcloud.io/dashboard?id=com.thiagoteixeira%3Ajson-diff-service-parent&nocache
[sonar-quality-gate]: https://sonarcloud.io/api/project_badges/measure?project=com.thiagoteixeira%3Ajson-diff-service-parent&metric=alert_status
[sonar-coverage]: https://sonarcloud.io/api/project_badges/measure?project=com.thiagoteixeira%3Ajson-diff-service-parent&metric=coverage&cached=1
[sonar-bugs]: https://sonarcloud.io/api/project_badges/measure?project=com.thiagoteixeira%3Ajson-diff-service-parent&metric=bugs
[sonar-vulnerabilities]: https://sonarcloud.io/api/project_badges/measure?project=com.thiagoteixeira%3Ajson-diff-service-parent&metric=vulnerabilities


<!-- GitHub issues -->
[i1]: https://github.com/thiagoteixeira/json-diff-service/issues/1
[i2]: https://github.com/thiagoteixeira/json-diff-service/issues/2
[i3]: https://github.com/thiagoteixeira/json-diff-service/issues/3
[i7]: https://github.com/thiagoteixeira/json-diff-service/issues/7
[i9]: https://github.com/thiagoteixeira/json-diff-service/issues/9
[i12]: https://github.com/thiagoteixeira/json-diff-service/issues/12