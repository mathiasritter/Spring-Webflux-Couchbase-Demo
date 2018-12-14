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

## How to configure couchbase
1. Download and install Couchbase
2. Run through the initial setup and navigate to the dashboard on `http://127.0.0.1:8091`
3. In `Buckets`: create a new bucket called `demo`
4. In `Security`: Add a new user called `demouser` with password `123456` and grant application access to the bucket `demo`
5. In `Query`: Add a primary index to the bucket `demo` by executing the query `CREATE PRIMARY INDEX ON demo USING GSI;`
6. In `Indexes` -> `Views`: Add a new development view
    - design document name is `person`
    - view name is `all`
    - edit the view and change the code of Map to `function (doc, meta) { emit(meta.id, doc); }`
7. In `Indexes` -> `Views`: Publish the view

## How to run
Before you run the application first, make sure that you configured couchbase as described above.
1. Start couchbase on the default port 8091
2. Run ` ./mvnw spring-boot:run` and Spring Boot will start on port 8080

## How to send requests

The API is queried by sending HTTP requests to `127.0.0.1:8080` followed by the URI listed below.

### Endpoints

| Intention       | HTTP Method  | URI             | Request Body       |
| :---------------|:------------:| :---------------| :-----------------:|
| Get all persons | GET          | `/persons `     | empty              |
| Get person      | GET          | `/persons/{id}` | empty              |
| Create person   | POST         | `/persons `     | Person (see below) |
| Update person   | PUT          | `/persons/{id}` | Person (see below) |
| Delete person   | DELETE       | `/persons/{id}` | empty              |

Note that the `{id}` must be replaced with the corresponding id of the person.

#### Person Request Body

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


