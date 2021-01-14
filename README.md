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


## FACADE - API Resources

  - [POST /v1/diff/[id]/left](#post-v1diffidleft)
  - [POST /v1/diff/[id]/right](#post-v1diffidright)
  - [GET /v1/diff/[id]](#get-v1diffid)

### POST {facade-host}/v1/diff/{id}/left

Example: http://localhost:8080/v1/diff/1/left
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

Example: http://localhost:8080/v1/diff/1/right
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

Example: http://localhost:8080/v1/diff/1
```
curl --location --request GET 'localhost:8080/v1/diff/22' \
--header 'traceId: test-thiago-teixeira-01' \
--header 'Accept-Language: en'
```
Response body:

    {
        "message": "The JSON contents are equal!"
    }

Note: result when both sides are equal!    


[sonar-url]:https://sonarcloud.io/dashboard?id=com.thiagojavabr%3Ajson-diff-service&nocache
[sonar-quality-gate]: https://sonarcloud.io/api/project_badges/measure?project=com.thiagojavabr%3Ajson-diff-service&metric=alert_status
[sonar-coverage]: https://sonarcloud.io/api/project_badges/measure?project=com.thiagojavabr%3Ajson-diff-service&metric=coverage&cached=1
[sonar-bugs]: https://sonarcloud.io/api/project_badges/measure?project=com.thiagojavabr%3Ajson-diff-service&metric=bugs
[sonar-vulnerabilities]: https://sonarcloud.io/api/project_badges/measure?project=com.thiagojavabr%3Ajson-diff-service&metric=vulnerabilities
