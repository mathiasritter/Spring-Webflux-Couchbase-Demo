# Spring Webflux Couchbase Demo

This Spring Boot demo project exposes a RESTful web API to apply CRUD (create, read, update, delete) operations on a
simple `Person` object, which is persisted in a couchbase database. The implementation was done in the reactive Webflux
stack, which is an alternative to the more commonly known Spring Web MVC stack. For more information about Webflux,
visit https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html

## Project Structure

The project is structured as follows:

- Web Layer
  - `PersonRouter` assigns URIs to the handler methods of the `PersonHandler`
  - `PersonHandler` handles incoming requests using the `PersonService`, some handlers also use the `RequestHandler`
  - `RequestHandler` contains a wrapper method to validate the body of a request
  - `GlobalErrorAttributes` defines how to handle exceptions and maps them to responses
- Service Layer
  - `PersonService` uses the `PersonRepository` for providing services to the `PersonHandler`
- Repository Layer
  - `Person` represents the model class for a person, which is going to be persisted in the database
  - `MyCouchbaseConfig` contains configuration for connecting to the database
  - `PersonRepository` contains CRUD methods for interacting with the database

In addition, the `CouchbaseDemoApplication` is the entry point of the application.

## How to run
1. Start couchbase on the default port 8091
2. Run ` ./mvnw spring-boot:run` and Spring Boot will start on port 8080

## How to send requests

The API is queried by sending HTTP requests to `127.0.0.1:8080` followed by the URI listed below.

#### Endpoints

| Intention       | HTTP Method  | URI            | Request Body       |
| :---------------|:------------:| :--------------| :-----------------:|
| Get all persons | GET          | `/persons `    | empty              |
| Get person      | GET          | `/person/{id}` | empty              |
| Create person   | POST         | `/persons `    | Person (see below) |
| Update person   | PUT          | `/person/{id}` | Person (see below) |
| Delete person   | DELETE       | `/person/{id}` | empty              |

Note that the `{id}` must be replaced with the corresponding id of the person.

##### Person Request Body

The Person request body is in JSON and must be structured as follows:

| attribute   | validations                                  |
| :-----------| :--------------------------------------------|
| `id`        | required for create, not required for update |
| `lastName`  | required, max. 30 characters                 |
| `firstName` | required, max. 30 characters                 |

Example:
```json
{
   "id": "p1",
   "lastName": "John",
   "firstName": "Doe"
}
```

