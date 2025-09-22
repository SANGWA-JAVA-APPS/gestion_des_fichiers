# Gestion des Fichiers - File Management System

This is a Spring Boot application for file management system based on the lawyer-backend architecture.

## Features

- **MariaDB Database Integration**: Configured to use MariaDB database
- **JPA Entities**: Account and AccountCategory entities with proper relationships
- **Security**: Basic authentication with JWT support
- **Swagger UI**: Secured API documentation
- **Default Users**: Pre-configured admin, manager, and user accounts
- **RESTful APIs**: Complete file management operations

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MariaDB 10.6 or higher

## Database Setup

1. Install MariaDB Server
2. The application will create the database `gestiondesfichier_db` automatically
3. Update the database credentials in `application.properties` if needed:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=A.manigu125
   ```

## Running the Application

1. Navigate to the project directory
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

The application will start on port 8104.

## Default Users

The application creates default users with BCrypt encoded passwords:

| Username | Password   | Role    |
|----------|------------|---------|
| admin    | admin123   | ADMIN   |
| manager  | manager123 | MANAGER |
| user     | user123    | USER    |

## API Documentation

Access the Swagger UI at: `http://localhost:8104/swagger-ui.html`

**Note**: Authentication is required to access the API documentation. Use any of the default users above.

## API Endpoints

### Public Endpoints
- `GET /` - Welcome message
- `GET /health` - Health check

### Authentication Endpoints
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout  
- `GET /api/auth/user` - Get current user info

### File Management Endpoints (Authenticated)
- `GET /api/files` - Get user files
- `GET /api/files/{id}` - Get file by ID
- `GET /api/files/stats` - Get file statistics

## Configuration

Key configuration properties in `application.properties`:

```properties
# Server
server.port=8104

# Database
spring.datasource.url=jdbc:mariadb://localhost:3300/gestiondesfichier_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=A.manigu125

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html

# JWT
jwt.secret=YXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZg==
jwt.expiration=86400000
```

## Security

The application uses Spring Security with Basic Authentication:
- Swagger UI requires authentication
- Public endpoints: Home and Health check
- Protected endpoints: All API endpoints under `/api/`

## Technologies Used

- Spring Boot 3.3.4
- Spring Data JPA
- Spring Security
- MariaDB
- Swagger/OpenAPI 3
- JWT (JSON Web Tokens)
- Lombok
- Maven

## Architecture

This application follows the same architectural patterns as the lawyer-backend system:
- Controller layer for REST endpoints
- Service layer for business logic
- Repository layer for data access
- Entity layer for JPA mappings
- Configuration classes for security and documentation

Instead of DTOs, the application uses interfaces for repository operations as specified in the requirements.