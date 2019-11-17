# Scalable Web

Scalable web is a Java demo application for [WAES](https://www.wearewaes.com/). 

The goal of this assignment is to show my coding skills and what I value in software engineering.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java 8
- Git

### Installing

- Clone the [source repository](https://github.com/brudenick/scalable-web) from Github. 
    - On the command line, type:
    ````
    git clone https://github.com/brudenick/scalable-web.git
    ````

- Open a terminal/console/command prompt, change to the directory where you cloned scalable-web, and type:
    ````
    ./mvnw clean install
    ````
  
### Running tests
- To run all tests:
    ````
    ./mvnw verify
    ````
- To run integration tests only:
    ````
    ./mvnw clean test-compile failsafe:integration-test
    ````
  
### Running the application
- Open a terminal/console/command prompt, change to the directory where you cloned scalable-web, and type:
    ````
    ./mvnw spring-boot:run
    ````

This starts embedded Tomcat listening on http port 8080 with empty context path. Server port number and
context path can be changed setting the following properties in application.properties file.

````
server.port=8080
server.servlet.context-path=/context-path
````

## REST API

See [Swagger-UI](http://localhost:8080/swagger-ui.html)

####Put Left Diff Part

`PUT /v1/diff/{id}/left`

Creates/updates a diff object associated to the given {id} and sets the LEFT part of it with the content of the request body.

Request:
```bash
curl -X PUT http://localhost:8080/v1/diff/123/left
    --header "Content-Type:application/octet-stream"
    --data-binary @data.file
````
Response:
```
HTTP/1.1 
Status: 201 CREATED
Content-Length: 0
<Response body is empty>
Response code: 201; Time: 556ms; Content length: 0 bytes
````

####Put Right Diff Part

`PUT /v1/diff/{id}/right`

Creates/updates a diff object associated to the given {id} and sets the RIGHT part of it with the content of the request body.

Request:
```bash
curl -X PUT http://localhost:8080/v1/diff/123/right
    --header "Content-Type:application/octet-stream"
    --data-binary @data.file
````
Response:
```
HTTP/1.1 
Status: 201 CREATED
Content-Length: 0
<Response body is empty>
Response code: 201; Time: 556ms; Content length: 0 bytes
````
####Get Diff Results
`GET /v1/diff/{id}`

Gets the results of the diff associated to the given {id}

Request:
```bash
curl -v http://localhost:8080/v1/diff/123
````

Response:
````
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 17 Nov 2019 12:52:18 GMT

{
  "diffId": 123,
  "message": "Left and right are equal",
  "differences": []
}

Response code: 200; Time: 50ms; Content length: 68 bytes
````