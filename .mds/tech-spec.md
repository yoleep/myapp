# Technical Specification for Common Module Development

## 1. Project Overview

This document defines the technical specifications for common module development. The frontend uses React, the backend uses Spring Boot, and the database can be selectively configured with H2 (development) and PostgreSQL (production).

## 2. Technology Stack

### 2.1 Frontend (React)

#### 2.1.1 Core Technologies
- **React**: 18.x
- **TypeScript**: 5.x
- **Package Manager**: npm/yarn
- **Build Tool**: Vite or Create React App

#### 2.1.2 State Management
- **Redux Toolkit**: Global state management
- **React Query (TanStack Query)**: Server state management and caching
- **Context API**: Local state management

#### 2.1.3 UI/UX
- **Design System**: Material-UI (MUI) or Ant Design
- **Styling**: Styled-Components / Emotion / CSS Modules
- **Form Handling**: React Hook Form
- **Validation**: Yup / Zod

#### 2.1.4 Routing and Navigation
- **React Router**: v6.x
- **Protected Routes**: Authentication-based route protection

#### 2.1.5 HTTP Client
- **Axios**: REST API communication
- **WebSocket**: Socket.io-client (real-time communication)

#### 2.1.6 Development Tools
- **ESLint**: Code quality management
- **Prettier**: Code formatting
- **Husky**: Git hooks management
- **Jest & React Testing Library**: Unit/integration testing

#### 2.1.7 Build and Deployment
- **Docker**: Containerization
- **Nginx**: Static file serving
- **CI/CD**: GitHub Actions / Jenkins

### 2.2 Backend (Spring Boot)

#### 2.2.1 Core Technologies
- **Spring Boot**: 3.x
- **Java**: 17 LTS
- **Build Tool**: Gradle / Maven

#### 2.2.2 Spring Ecosystem
- **Spring Web**: RESTful API development
- **Spring Data JPA**: ORM and database access
- **Spring Security**: Authentication and authorization
- **Spring Validation**: Input data validation
- **Spring Boot Actuator**: Application monitoring

#### 2.2.3 Database Integration
- **JPA/Hibernate**: ORM framework
- **QueryDSL**: Type-safe query creation
- **Flyway/Liquibase**: Database migration

#### 2.2.4 Authentication and Security
- **JWT**: JSON Web Token based authentication
- **OAuth 2.0**: Social login
- **Spring Security**: Permission management
- **BCrypt**: Password encryption

#### 2.2.5 API Documentation
- **SpringDoc OpenAPI**: Automatic Swagger UI generation
- **REST Docs**: API documentation

#### 2.2.6 Logging and Monitoring
- **SLF4J + Logback**: Logging
- **Micrometer**: Metrics collection
- **Spring Boot Admin**: Application monitoring

#### 2.2.7 Testing
- **JUnit 5**: Unit testing
- **Mockito**: Mocking framework
- **Spring Boot Test**: Integration testing
- **RestAssured**: API testing

### 2.3 Database

#### 2.3.1 Development Environment (H2 Database)
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: true
```

#### 2.3.2 Production Environment (PostgreSQL)
```yaml
# application-prod.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/proddb
    driver-class-name: org.postgresql.Driver
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
```

#### 2.3.3 Database Switching Strategy
- **Profile-based configuration**: Environment-specific DB settings via Spring Profiles
- **Environment variables**: Sensitive information managed through environment variables
- **Connection Pool**: Using HikariCP
- **Migration**: Schema version control through Flyway

## 3. Common Module Architecture

### 3.1 Project Structure

#### Frontend Structure
```
frontend/
├── src/
│   ├── components/       # Reusable components
│   │   ├── common/       # Common UI components
│   │   ├── forms/        # Form-related components
│   │   └── layout/       # Layout components
│   ├── pages/            # Page components
│   ├── services/         # API services
│   ├── hooks/            # Custom hooks
│   ├── store/            # Redux store
│   ├── utils/            # Utility functions
│   ├── types/            # TypeScript type definitions
│   └── constants/        # Constant definitions
├── public/               # Static files
├── tests/                # Test files
└── package.json
```

#### Backend Structure
```
backend/
├── src/main/java/com/company/common/
│   ├── config/           # Configuration classes
│   ├── controller/       # REST controllers
│   ├── service/          # Business logic
│   ├── repository/       # Data access layer
│   ├── entity/           # JPA entities
│   ├── dto/              # Data transfer objects
│   ├── exception/        # Exception handling
│   ├── security/         # Security-related
│   ├── util/             # Utility classes
│   └── validation/       # Validation logic
├── src/main/resources/
│   ├── application.yml   # Default configuration
│   ├── application-dev.yml  # Development configuration
│   ├── application-prod.yml # Production configuration
│   └── db/migration/     # Flyway migration scripts
└── src/test/             # Test code
```

### 3.2 Common Module Architecture

The common modules provide foundational services and utilities that are used across the application. These modules are designed to be reusable, maintainable, and scalable. For detailed functional specifications of each module, please refer to `function-spec.md`.

#### Module Categories:
- **Authentication/Authorization**: Security and access control
- **Error Handling**: Centralized error management
- **Logging**: Comprehensive logging system
- **Utilities**: Common helper functions
- **Validation**: Data validation framework

### 3.3 API Design Principles

#### 3.3.1 RESTful API Rules
- Use noun-based resources
- Utilize HTTP methods (GET, POST, PUT, DELETE, PATCH)
- Standardize status codes
- Version management (/api/v1/)

#### 3.3.2 Response Format Standardization
```json
{
  "success": true,
  "data": {
    // Actual data
  },
  "message": "Success message",
  "timestamp": "2024-01-01T12:00:00Z"
}
```

#### 3.3.3 Error Response Format
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Error message",
    "details": []
  },
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## 4. Development Environment Setup

### 4.1 Required Tools
- Node.js 18+ (Frontend)
- Java 17+ (Backend)
- PostgreSQL 14+
- Docker & Docker Compose
- Git

### 4.2 IDE Configuration
- **Frontend**: VS Code + ESLint + Prettier plugins
- **Backend**: IntelliJ IDEA + Spring Boot plugin

### 4.3 Environment Variable Management
```bash
# .env.development
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_ENVIRONMENT=development

# .env.production
REACT_APP_API_URL=https://api.production.com/api
REACT_APP_ENVIRONMENT=production
```

## 5. Build and Deployment

### 5.1 Docker Configuration

#### Frontend Dockerfile
```dockerfile
# Build stage
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

#### Backend Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILE}", "app.jar"]
```

#### Docker Compose
```yaml
version: '3.8'
services:
  frontend:
    build: ./frontend
    ports:
      - "3000:80"
    depends_on:
      - backend
    environment:
      - REACT_APP_API_URL=http://backend:8080/api

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=postgres
      - DB_PORT=5432
      - DB_NAME=appdb
      - DB_USERNAME=appuser
      - DB_PASSWORD=apppass

  postgres:
    image: postgres:14-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=appdb
      - POSTGRES_USER=appuser
      - POSTGRES_PASSWORD=apppass
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

## 6. Testing Strategy

### 6.1 Frontend Testing
- **Unit Tests**: Components, functions, hooks
- **Integration Tests**: Page-level testing
- **E2E Tests**: Cypress/Playwright

### 6.2 Backend Testing
- **Unit Tests**: Service, Repository layers
- **Integration Tests**: Controller testing
- **API Tests**: RestAssured

### 6.3 Test Coverage Goals
- Unit tests: 80% or higher
- Integration tests: 60% or higher
- E2E tests: 100% core scenarios

## 7. Security Considerations

### 7.1 Frontend Security
- XSS prevention
- CSRF token usage
- No client-side storage of sensitive information
- Force HTTPS usage

### 7.2 Backend Security
- SQL Injection prevention (Prepared Statements)
- Input data validation
- API Rate Limiting
- CORS configuration
- Sensitive data encryption

## 8. Performance Optimization

### 8.1 Frontend Optimization
- Code Splitting
- Lazy Loading
- Image optimization
- Caching strategy
- Bundle size optimization

### 8.2 Backend Optimization
- Database indexing
- Query optimization
- Caching (Redis)
- Connection Pool tuning
- JVM tuning

## 9. Monitoring and Logging

### 9.1 Monitoring Tools
- **APM**: New Relic / DataDog
- **Log Collection**: ELK Stack (Elasticsearch, Logstash, Kibana)
- **Metrics**: Prometheus + Grafana

### 9.2 Alert Configuration
- Error rate threshold exceeded
- Response time increase
- Server resource utilization
- Database connection pool exhaustion

## 10. Development Guidelines

### 10.1 Code Convention
- **Frontend**: Airbnb JavaScript Style Guide
- **Backend**: Google Java Style Guide

### 10.2 Git Branch Strategy
- main: Production deployment branch
- develop: Development integration branch
- feature/*: Feature development branches
- hotfix/*: Emergency fix branches

### 10.3 Commit Message Rules
```
<type>: <subject>

<body>

<footer>
```

Types:
- feat: New feature
- fix: Bug fix
- docs: Documentation update
- style: Code formatting
- refactor: Code refactoring
- test: Test addition
- chore: Build task modification

## 11. Project Timeline

### Phase 1: Initial Setup (1-2 weeks)
- Development environment configuration
- Project structure setup
- CI/CD pipeline construction

### Phase 2: Common Module Development (3-4 weeks)
- Authentication/authorization module
- Error handling module
- Logging module
- Utility module

### Phase 3: Core Feature Development (4-6 weeks)
- Core feature implementation (see function-spec.md for details)

### Phase 4: Testing and Optimization (2-3 weeks)
- Test code creation
- Performance optimization
- Security check
- Documentation

### Phase 5: Deployment and Stabilization (1-2 weeks)
- Staging environment deployment
- Bug fixes
- Production environment deployment
- Monitoring setup

## 12. References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [H2 Database Documentation](http://www.h2database.com/)
- [Docker Documentation](https://docs.docker.com/)