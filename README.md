# RentRead

RentRead is a backend REST API that powers an online book rental platform. Users can register,
browse available books, rent them, and return them — all through a clean set of HTTP endpoints
secured with Basic Authentication and role-based authorization.

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen?logo=springboot)  
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql&logoColor=white)  
![Gradle](https://img.shields.io/badge/Gradle-8.11.1-02303A?logo=gradle)
  
---  

## Key Features

| Feature                        | Description                                                        |  
|--------------------------------|--------------------------------------------------------------------|  
| **User Registration & Login**  | Sign up with email/password; passwords hashed with BCrypt          |  
| **Role-Based Access Control**  | Two roles — `USER` and `ADMIN` — with endpoint-level authorization |  
| **Book Management**            | Full CRUD for books (Admin only for create/update/delete)          |  
| **Rental System**              | Rent and return books with automatic availability tracking         |  
| **Rental Limit Enforcement**   | Users are limited to 2 active rentals at a time                    |  
| **Global Error Handling**      | Consistent error responses via `@ControllerAdvice`                 |  
| **DTO Pattern with MapStruct** | Clean separation between API contracts and database entities       |  
| **Logging**                    | SLF4J/Logback logging across all layers                            |  
| **Unit Tests**                 | MockMvc + Mockito tests for controllers                            |  

  
---  

## Tech Stack

- **Language:** Java 17 (OpenJDK 17.0.7)
- **Framework:** Spring Boot 3.4.1
- **Security:** Spring Security (HTTP Basic Auth)
- **ORM:** Spring Data JPA / Hibernate
- **Database:** MySQL 8.0
- **Build Tool:** Gradle 8.11.1 (Wrapper)
- **Mapping:** MapStruct 1.5.5
- **Utilities:** Lombok, Jakarta Validation
- **Testing:** JUnit 5, MockMvc, Mockito

---    

## ER Diagram

Use below erDiagram code in [mermaid.live](https://mermaid.live/edit) website to see erDiagram.

  ```erDiagram
  erDiagram
    USERS {
        BIGINT id PK "Auto Increment"
        VARCHAR first_name "NOT NULL"
        VARCHAR last_name "NOT NULL"
        VARCHAR email "NOT NULL, UNIQUE"
        VARCHAR password "NOT NULL (BCrypt hashed)"
        ENUM role "USER | ADMIN"
    }

    BOOKS {
        BIGINT id PK "Auto Increment"
        VARCHAR title "NOT NULL"
        VARCHAR author "NOT NULL"
        ENUM genre "FICTION | NON_FICTION | SCIENCE | ..."
        ENUM availability_status "AVAILABLE | NOT_AVAILABLE"
    }

    RENTALS {
        BIGINT id PK "Auto Increment"
        BIGINT user_id FK "NOT NULL"
        BIGINT book_id FK "NOT NULL"
        DATE rented_at "NOT NULL"
        DATE return_date "NULL if active"
    }

    USERS ||--o{ RENTALS : "rents"
    BOOKS ||--o{ RENTALS : "rented in"
  ```

  
---  

## Getting Started

### Prerequisites

- **Java 21**
- **MySQL 8.0**
- **Git**

> **Note:** No need to install Gradle — the project includes a Gradle Wrapper.

### Installation

1. **Clone the repository**

   ```bash  
   git clone https://github.com/<your-username>/rentread.git
   cd rentread
   ```  

2. **Create the MySQL database**

   ```plaintext  
   CREATE DATABASE IF NOT EXISTS rentread_db;
   ```  

3. **Configure database credentials**

   Edit `src/main/resources/application.properties`:

   ```properties  
   spring.datasource.url=jdbc:mysql://localhost:3306/rentread_db?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```  

### Running the Application

```bash  
# Linux / macOS  
./gradlew bootRun  
  
# Windows  
gradlew.bat bootRun  
```  

The API will start at **`http://localhost:8081/`**.

### Running Tests

```bash  
./gradlew test
```  

  
---  

## API Reference

**Base URL:** `http://localhost:8081`

All private endpoints use **HTTP Basic Authentication**. Include your credentials with every
request:

```bash  
curl -u user@example.com:password123 http://localhost:8081/books
```  

### Authentication

| Method | Endpoint       | Access | Description                    |  
|--------|----------------|--------|--------------------------------|  
| `POST` | `/auth/signup` | Public | Register a new user or admin   |  
| `POST` | `/auth/login`  | Public | Login and validate credentials |  

<details>  
<summary><strong>POST /auth/signup</strong> — Register a new account</summary>  

**Request:**

```json  
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}  
```  

> Omit `"role"` to default to `USER`, or set `"role": "ADMIN"` for admin access.

**Response (`201 Created`):**

```json  
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "user@example.com",
  "role": "USER"
}  
```  

</details>  

<details>  
<summary><strong>POST /auth/login</strong> — Login</summary>  

**Request:**

```json  
{
  "email": "user@example.com",
  "password": "password123"
}  
```  

**Response (`200 OK`):**

```json  
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "user@example.com",
  "role": "USER"
}  
```  

</details>  
  
---  

### Books

| Method   | Endpoint      | Access        | Description       |  
|----------|---------------|---------------|-------------------|  
| `GET`    | `/books`      | Authenticated | List all books    |  
| `GET`    | `/books/{id}` | Authenticated | Get a book by ID  |  
| `POST`   | `/books`      | Admin only    | Create a new book |  
| `PUT`    | `/books/{id}` | Admin only    | Update a book     |  
| `DELETE` | `/books/{id}` | Admin only    | Delete a book     |  

<details>  
<summary><strong>POST /books</strong> — Create a book (Admin only)</summary>  

**Request:**

```json  
{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "genre": "FICTION",
  "availabilityStatus": "AVAILABLE"
}  
```  

**Response (`201 Created`):**

```json  
{
  "id": 1,
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "genre": "FICTION",
  "availabilityStatus": "AVAILABLE"
}  
```  

</details>  

> **Genres:** `FICTION`, `NON_FICTION`, `HISTORY`, `FANTASY`, `MYSTERY`, `ROMANCE`, `THRILLER`,
`OTHER`
  
---  

### Rentals

| Method | Endpoint                                 | Access        | Description               |  
|--------|------------------------------------------|---------------|---------------------------|  
| `POST` | `/rentals/users/{userId}/books/{bookId}` | Authenticated | Rent a book               |  
| `PUT`  | `/rentals/{rentalId}`                    | Authenticated | Return a rented book      |  
| `GET`  | `/rentals/active-rentals/users/{userId}` | Authenticated | Get user's active rentals |  

<details>  
<summary><strong>POST /rentals/users/{userId}/books/{bookId}</strong> — Rent a book</summary>  

**Response (`201 Created`):**

```json  
{
  "id": 1,
  "book": {
    "id": 1,
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "genre": "FICTION",
    "availabilityStatus": "NOT_AVAILABLE"
  },
  "rentedAt": "2026-04-12",
  "returnDate": null
}  
```  

**Error — Rental limit reached (`400 Bad Request`):**

```json  
{
  "message": "User has already reached maximum book rental limit!",
  "httpStatus": "BAD_REQUEST",
  "localDateTime": "2026-04-12T10:30:00.000"
}  
```  

</details>  
  
---  

### Error Responses

All errors follow a consistent format:

```json  
{
  "message": "Descriptive error message",
  "httpStatus": "HTTP_STATUS_NAME",
  "localDateTime": "2026-04-12T10:30:00.000"
}  
```  

| HTTP Code | When                                                          |  
|-----------|---------------------------------------------------------------|  
| `400`     | Validation error, rental limit exceeded, book not available   |  
| `401`     | Invalid credentials                                           |  
| `403`     | Insufficient permissions (e.g., USER trying to create a book) |  
| `404`     | Resource not found (user, book, or rental)                    |  
| `409`     | Duplicate resource (e.g., email already registered)           |  

  
---  