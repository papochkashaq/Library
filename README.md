# Library

A web application for library management: tracking books, readers, and book lending. Built with Spring Boot using server-side rendering via Thymeleaf.

## Features

- **Book catalog**: browse the list of books with pagination and optional sorting by publication year.
- **Book details**: view detailed information about a book and its current owner.
- **Add / edit / delete books** with field validation (title, author, publication year).
- **Search books by title** (partial match).
- **Lending and returning books**: assign a reader as the book's owner with the lending date recorded, and release the book from the reader.
- **Overdue tracking**: a book is considered overdue if more than 10 days have passed since the lending date (see [`Book.isOverdue()`](src/main/java/com/alderson/library/model/Book.java:39)).
- **Reader management**: browse the list, add, edit, and delete readers with validation (unique full name, valid birth year).
- **Automatic book release** when a reader is deleted (see [`Person.releaseBooks()`](src/main/java/com/alderson/library/model/Person.java:35)).

## Tech Stack

- **Java 25**
- **Spring Boot 4.0.5**
  - Spring Web / Spring MVC
  - Spring Data JPA
  - Spring Boot Validation
  - Thymeleaf (view templating engine)
- **PostgreSQL** — primary database
- **Lombok** — reduces model boilerplate code
- **Maven** — project build, packaged as `war`

## Requirements

- JDK 25+
- Maven 3.9+ (or use the Maven Wrapper included in the project — [`mvnw`](mvnw) / [`mvnw.cmd`](mvnw.cmd))
- PostgreSQL 12+ (running locally or accessible over the network)

## Project Structure

```
src/main/java/com/alderson/library/
├── LibraryApplication.java      # Spring Boot entry point
├── ServletInitializer.java      # Initialization for WAR deployment
├── controller/
│   ├── IndexController.java     # Home page
│   ├── BookController.java      # CRUD and operations on books
│   └── PersonController.java    # CRUD on readers
├── model/
│   ├── Book.java                # Book entity
│   └── Person.java              # Reader entity
├── repository/
│   ├── BookRepository.java      # JPA repository for books
│   └── PersonRepository.java    # JPA repository for readers
└── service/
    ├── BookService.java         # Business logic for books
    └── PersonService.java       # Business logic for readers

src/main/resources/
├── application.properties       # Application configuration
└── templates/                   # Thymeleaf templates (index, books/*, people/*)
```

## Database Setup

Before running the application, create a PostgreSQL database and adjust the connection settings in [`application.properties`](src/main/resources/application.properties) if needed:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=0451
```

The database schema is created and updated automatically thanks to the `spring.jpa.hibernate.ddl-auto=update` setting, so no separate SQL migrations are required.

> ⚠️ By default, the configuration file contains a login and password for local development. For a production environment, be sure to replace them with actual credentials (for example, via environment variables or Spring profiles).

## Running the Project

### Using Maven Wrapper

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

### Using an installed Maven

```bash
mvn spring-boot:run
```

Once started, the application will be available at:

```
http://localhost:8081
```

The port can be changed in [`application.properties`](src/main/resources/application.properties:2) (the `server.port` parameter).

## Build

The project is built as a `war` artifact:

```bash
mvnw.cmd clean package
```

The resulting file will be located in the `target/` directory.

## Main Routes

| Method | Path | Description |
|---|---|---|
| `GET` | `/` | Home page |
| `GET` | `/books` | List of books (parameters `page`, `books_per_page`, `sort_by_year`) |
| `GET` | `/books/{id}` | Book details |
| `GET` | `/books/new` | Add book form |
| `POST` | `/books/new` | Save a new book |
| `GET` | `/books/edit/{id}` | Edit book form |
| `PATCH` | `/books/{id}` | Update a book |
| `PATCH` | `/books/{id}/add-owner` | Assign a reader as the book's owner |
| `PATCH` | `/books/{id}/delete-owner` | Release the book from its owner |
| `DELETE` | `/books/{id}` | Delete a book |
| `GET` / `POST` | `/books/search` | Search for a book by title |
| `GET` | `/people` | List of readers |
| `GET` | `/people/new` | Add reader form |
| `POST` | `/people/new` | Save a new reader |
| `GET` | `/people/{id}` | Reader details |
| `GET` | `/people/edit/{id}` | Edit reader form |
| `PATCH` | `/people/{id}` | Update a reader |
| `DELETE` | `/people/{id}` | Delete a reader |

> `PATCH` and `DELETE` methods from HTML forms are passed via the hidden `_method` field thanks to the `spring.mvc.hiddenmethod.filter.enabled=true` setting.

## Testing

```bash
mvnw.cmd test
```
