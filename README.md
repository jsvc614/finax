# Finax - Backend Todo Application

## 📋 Brief Description

Finax is a robust backend financial application built with Spring Boot 4.0.2. It provides a secure REST API for todo/task management with JWT-based authentication, rate limiting, and database migrations using Flyway.

The application demonstrates enterprise-grade practices including:
- Secure JWT token-based authentication
- Access control with Spring Security
- Rate limiting to prevent API abuse
- Database schema management with Flyway
- Comprehensive API documentation with Swagger/OpenAPI
- Caching for improved performance
- Input validation and error handling
- Pagination and filtering support
- Advanced search capabilities
- Docker setup

---

## 🛠 Technologies Used

### Core Framework
- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 4.0.2** - Rapid application development framework
- **Spring Data JPA** - ORM and data access layer
- **Spring Security** - Authentication and authorization

### Database
- **PostgreSQL 16** - Production-grade relational database
- **Flyway** - Database versioning and migration management
- **Hibernate** - JPA implementation with ORM capabilities

### Authentication & Security
- **JWT (JJWT 0.11.5)** - Stateless token-based authentication
- **Spring Security** - Authorization and secure endpoint protection

### API & Documentation
- **SpringDoc OpenAPI 3.0.1** - Swagger UI for interactive API documentation
- **Jackson** - JSON serialization/deserialization

### Performance & Caching
- **Caffeine 3.1.8** - High-performance in-memory cache
- **Bucket4j 8.7.0** - Rate limiting and throttling

### Development Tools
- **Lombok 1.18.36** - Boilerplate code reduction
- **Spring DevTools** - Hot reload and live development
- **Maven 3.6+** - Build automation and dependency management

### Testing
- **JUnit 5** - Modern Java testing framework
- **Spring Boot Test** - Integration testing utilities
- **Spring Security Test** - Security-specific testing

---

## 🚀 Setup Instructions

### Option 1: Local PostgreSQL Setup (Development)

#### Prerequisites
- Java 21 or higher
- PostgreSQL 12+ installed and running
- Maven 3.6+ or use the included Maven wrapper
- Git

#### Step 1: Clone Repository

```bash
git clone https://github.com/jsvc614/finax.git
cd finax
```

#### Step 2: Create PostgreSQL Database & User

```bash
psql -U postgres
```

Run these SQL commands:

```sql
CREATE DATABASE finax;
CREATE USER test WITH PASSWORD 'test';
ALTER ROLE test WITH CREATEDB;
GRANT ALL PRIVILEGES ON DATABASE finax TO test;

\q

\c finax
psql -U postgres -d finax

GRANT ALL PRIVILEGES ON SCHEMA public TO test;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO test;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO test;
GRANT USAGE ON SCHEMA public TO test;

ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO test;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON SEQUENCES TO test;
ALTER DATABASE finax OWNER TO test;

\q
```

#### Step 3: Configure Local Application Properties

Create/Update: `src/main/resources/application.properties`

```properties
# Application
spring.application.name=finax
server.port=8080

# PostgreSQL Local Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/finax
spring.datasource.username=test
spring.datasource.password=test
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Flyway Database Migrations
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true

# DevTools
spring.devtools.remote.secret=mysecret

# JWT Configuration
finax.jwtSecret=sZ+OWEfUj5PysUIzMkaBohypWo6C8AZwWS4K71mcVbdbK+mr4BdGUh6NKbxm+zTIK4KraJ5n7MHI1BU3KOsODQ==
finax.jwtAccessTokenValidity=86400000
```

#### Step 4: Build & Run

```bash
# Build the project (skip tests if needed)
./mvnw clean package -DskipTests

# Run the application
./mvnw spring-boot:run
```

On Windows:
```cmd
mvnw.cmd clean package -DskipTests
mvnw.cmd spring-boot:run
```

**Application will start at:** `http://localhost:8080`

---

### Option 2: Docker Setup (Recommended for Production)

#### Prerequisites
- Docker Desktop (Mac/Windows) or Docker + Docker Compose (Linux)
- Git

#### Step 1: Clone Repository

```bash
git clone https://github.com/jsvc614/finax.git
cd finax
```

#### Step 2: Configure Docker Application Properties

Create/Update: `src/main/resources/application.properties`

```properties
# Application
spring.application.name=finax
server.port=8080

# PostgreSQL Docker Configuration
spring.datasource.url=jdbc:postgresql://db:5432/finax
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Flyway Database Migrations
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true

# JWT Configuration
finax.jwtSecret=sZ+OWEfUj5PysUIzMkaBohypWo6C8AZwWS4K71mcVbdbK+mr4BdGUh6NKbxm+zTIK4KraJ5n7MHI1BU3KOsODQ==
finax.jwtAccessTokenValidity=86400000
```

#### Step 3: Verify Docker Files

**Dockerfile:**
```dockerfile
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# install maven
RUN apk add --no-cache maven bash git

# copy only pom first (for caching deps)
COPY pom.xml .

RUN mvn dependency:go-offline

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]
```

**docker-compose.yml:**
```yaml
services:
  db:
    image: postgres:16
    container_name: postgres_db
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: finax
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    container_name: finax_app
    ports:
      - "8080:8080"
    volumes:
      - .:/app
    depends_on:
      - db

volumes:
  postgres_data:
```

#### Step 4: Build & Start Docker

```bash

# Build and start containers
docker-compose up --build
```

To run in background:
```bash
docker-compose up -d
```
**Application will start at:** `http://localhost:8080`

---

## 📚 API Documentation

### Swagger UI

Once the application is running, access the interactive API documentation:

**URL:** `http://localhost:8080/swagger-ui.html`

This provides:
- Complete API endpoint listing
- Request/response schemas
- Try-it-out functionality
- Authentication configuration

### OpenAPI Specification

Raw OpenAPI specification available at:
`http://localhost:8080/v3/api-docs`

### Authentication

All endpoints (except login/register) require JWT token in the Authorization header:

```bash
# Get JWT token first
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}'

# Response shape:
# {
#   "success": true,
#   "data": { "token": "eyJhbGc..." },
#   "message": "Login successful"
# }

# Use token in subsequent requests
curl -H "Authorization: Bearer eyJhbGc..." \
  http://localhost:8080/api/todos
```

### Todo API Endpoints

#### Authentication Endpoints
- `POST /api/auth/login` - User login, returns wrapped response with JWT token in `data.token`
- `POST /api/auth/register` - User registration

#### Todo Endpoints (All require JWT authentication)

**Get All Todos (Paginated)**
```bash
GET /api/todos?status=completed&page=0&size=10&sort=createdAt,desc
```
- Query Parameters:
  - `status` (optional) - Filter by status: `completed`, `active`, or omit for all
  - `page` (optional) - Page number (default: 0)
  - `size` (optional) - Page size (default: 10)
  - `sort` (optional) - Sort by field and direction (default: `createdAt,desc`)

**Get All Active Todos (Paginated)**
```bash
GET /api/todos/active?status=active&page=0&size=10
```
- Returns only active (incomplete) todos
- Same pagination parameters as above

**Get Single Todo by ID**
```bash
GET /api/todos/{id}
```
- Path Parameters:
  - `id` (required) - Todo ID

**Create New Todo**
```bash
POST /api/todos
Content-Type: application/json

{
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "dueDate": "2026-02-20T10:00:00"
}
```

**Update Todo**
```bash
PUT /api/todos/{id}
Content-Type: application/json

{
  "title": "Buy groceries",
  "description": "Updated description",
  "dueDate": "2026-02-21T10:00:00"
}
```

**Delete Todo (Hard Delete)**
```bash
DELETE /api/todos/{id}
```
- Permanently removes todo from database

**Soft Delete Todo**
```bash
DELETE /api/todos/softDelete/{id}
```
- Marks todo as deleted without removing from database

**Toggle Todo Completion Status**
```bash
PATCH /api/todos/{id}/toggle
```
- Toggles the `completed` status of a todo

**Get Todo Statistics**
```bash
GET /api/todos/stats
```
- Returns statistics about todos:
  ```json
  {
    "totalTodos": 10,
    "completedTodos": 6,
    "activeTodos": 4,
  }
  ```

**Search Todos**
```bash
GET /api/todos/search?keyword=groceries
```
- Query Parameters:
  - `keyword` (required) - Search keyword (must not be blank)
- Returns list of todos matching the keyword in title or description

### Response Format

All API responses follow a standard format:

```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Buy groceries",
    "description": "Milk, eggs, bread",
    "completed": false,
    "dueDate": "2026-02-20T10:00:00",
    "createdAt": "2026-02-17T14:30:00",
    "updatedAt": "2026-02-17T14:30:00"
  },
  "message": "Todo created successfully"
}
```

Error Response:
```json
{
  "success": false,
  "error": "Validation failed",
  "errors": {
    "title": ["Title is required"]
  }
}
```

---

## 📁 Project Structure Overview

```
finax/
├── src/
│   ├── main/
│   │   ├── java/com/example/finax/
│   │   │   ├── FinaxApplication.java          # Main Spring Boot application
│   │   │   ├── auth/                           # JWT authentication components
│   │   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │   ├── JwtFilter.java
│   │   │   │   └── JwtUtil.java
│   │   │   ├── controller/                     # REST controllers
│   │   │   │   ├── AuthController.java
│   │   │   │   └── TodoController.java
│   │   │   ├── service/                        # Business logic
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── AuthServiceImpl.java
│   │   │   │   ├── TodoService.java
│   │   │   │   └── TodoServiceImpl.java
│   │   │   ├── repository/                     # Data access
│   │   │   │   ├── UserRepository.java
│   │   │   │   └── TodoRepository.java
│   │   │   ├── model/                          # JPA entities
│   │   │   │   ├── User.java
│   │   │   │   └── Todo.java
│   │   │   ├── dto/                            # Data transfer objects
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── MessageResponse.java
│   │   │   │   ├── SuccessResponse.java
│   │   │   │   ├── auth/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── LoginResponse.java
│   │   │   │   │   └── RegisterRequest.java
│   │   │   │   ├── todo/
│   │   │   │   │   ├── TodoDto.java
│   │   │   │   │   ├── TodoRequestDto.java
│   │   │   │   │   └── TodoStats.java
│   │   │   │   └── user/
│   │   │   │       └── UserDto.java
│   │   │   ├── security/                       # Security configuration
│   │   │   │   └── WebSecurityConfig.java
│   │   │   ├── exception/                      # Custom exceptions
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── InvalidCredentialsException.java
│   │   │   │   ├── RateLimitExceededException.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── UnauthorizedException.java
│   │   │   │   ├── UserAlreadyExistsException.java
│   │   │   │   └── BadRequestException.java
│   │   │   ├── config/                         # Application configuration
│   │   │   │   ├── OpenApiSecurityConfig.java
│   │   │   │   ├── RateLimitFilter.java
│   │   │   │   └── TodoSeeder.java
│   │   │   ├── mapper/                         # Entity/DTO mappers
│   │   │   │   ├── TodoMapper.java
│   │   │   │   └── UserMapper.java
│   │   │   └── util/                           # Utility classes
│   │   │       ├── CompletedFilterHelper.java
│   │   │       └── PageHelper.java
│   │   └── resources/
│   │       ├── application.properties          # Local config
│   │       ├── db/migration/                   # Flyway migrations
│   │       │   └── V1__1__init.sql
│   └── test/
│       ├── java/com/example/finax/
│       │   └── FinaxApplicationTests.java
├── pom.xml                                     # Maven dependencies
├── Dockerfile                                  # Docker image definition
├── docker-compose.yml                          # Docker services definition
├── mvnw                                        # Maven wrapper (Linux/Mac)
├── mvnw.cmd                                    # Maven wrapper (Windows)
├── HELP.md                                     # Project help file
└── README.md                         # This file
```

---

## 🎯 Design Decisions and Trade-offs

### 1. JWT Authentication vs Session-Based
**Decision:** JWT tokens (stateless)
- **Pros:** Scalable, microservices-friendly, works with distributed systems
- **Trade-off:** Can't revoke tokens immediately; requires careful token expiration management

### 2. Layered Architecture (Controller → Service → Repository)
**Decision:** Three-layer architecture
- **Pros:** Separation of concerns, testable, maintainable
- **Trade-off:** More boilerplate code compared to simpler architectures

### 3. Spring Data JPA vs Native Queries
**Decision:** Primarily JPA with Hibernate
- **Pros:** Database agnostic, type-safe, less SQL injection risk
- **Trade-off:** Slightly less control over query optimization for complex queries

### 4. Flyway for Database Migrations
**Decision:** Version-controlled SQL migrations
- **Pros:** Reproducible schema changes, team collaboration, easy rollbacks
- **Trade-off:** Manual SQL writing instead of ORM-generated schemas

### 5. Rate Limiting with Bucket4j
**Decision:** Token bucket algorithm
- **Pros:** Fair rate limiting, prevents API abuse, standard algorithm
- **Trade-off:** In-memory storage; doesn't persist across server restarts

### 6. Caffeine Caching
**Decision:** In-memory cache for frequently accessed data
- **Pros:** Ultra-fast performance, zero network latency
- **Trade-off:** Limited to single instance; doesn't work in distributed clusters

### 7. Docker Containerization
**Decision:** Containerized deployment with Docker Compose
- **Pros:** Consistency across environments, easy scaling, simplified deployment
- **Trade-off:** Slight performance overhead compared to native execution

### 8. Separate Configuration Profiles
**Decision:** Different `application.properties` for local and docker
- **Pros:** Environment-specific settings, no code changes needed
- **Trade-off:** Multiple config files to manage

### 9. Pagination and Filtering
**Decision:** Use Spring Data's Pageable for pagination
- **Pros:** Built-in support, easy to implement, RESTful convention
- **Trade-off:** Requires understanding of pagination concepts

### 10. Soft Delete Pattern
**Decision:** Support both hard and soft deletes
- **Pros:** Data recovery possible, audit trail maintained
- **Trade-off:** Requires additional logic to filter deleted records

---

## 🔮 Future Improvements

### Short-term 
- [ ] Add refresh token flow with token rotation and revocation list, use redis cache to handle token revocation logic
- [ ] Introduce role-based authorization for admin-only todo operations
- [ ] Add request correlation IDs and structured JSON logging
- [ ] Implement optimistic locking for todo updates to prevent overwrite conflicts
- [ ] Add validation for due-date rules (e.g., past date handling) and consistent error codes
- [ ] Expand automated tests for service and controller layers (success + edge cases)
- [ ] CORS configuration: allow only trusted frontend origins per environment, restrict methods/headers, disable wildcard origins in production

### Mid-term 
- [ ] Add Redis-backed rate limiting/cache for multi-instance deployments
- [ ] Add audit trail for critical actions (create/update/delete/login)
- [ ] Add API versioning strategy (e.g., `/api/v1`) to support safe evolution
- [ ] Improve search with indexed queries and optional full-text support
- [ ] Add audit logging for critical events (login, create, update, delete, toggle)
---
