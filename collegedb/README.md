# CollegeDB

Spring Boot CRUD API for managing core college data such as users, students, faculty, departments, courses, fees, and timetables.

## What Changed

- Externalized database and server configuration through environment variables.
- Added a safer default JPA setting for non-development environments.
- Introduced password hashing and a service layer for the `users` API.
- Added request DTO validation and centralized API error responses.
- Cleaned up naming and repository hygiene basics.

## Run Locally

1. Set environment variables:
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - Optional: `JPA_DDL_AUTO`, `JPA_SHOW_SQL`, `SERVER_PORT`
2. For local development, run with the `dev` profile to keep the previous schema-update behavior:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

3. Open Swagger UI at `http://localhost:8080/swagger-ui.html` or `http://localhost:8080/swagger-ui/index.html`.

## Configuration Defaults

- `application.properties` uses environment-variable placeholders.
- `application-dev.properties` enables `spring.jpa.hibernate.ddl-auto=update` and SQL logging for local development only.

## Users API Notes

- Passwords are now hashed before persistence.
- User create/update requests validate username, password length, and role.
- Supported roles are `ADMIN`, `STUDENT`, `FACULTY`, and `PARENT`.

## Testing

Run:

```powershell
.\mvnw.cmd test
```

If Maven cannot write to your default `.m2` directory in your environment, point it to a project-local repository:

```powershell
.\mvnw.cmd "-Dmaven.repo.local=.m2" test
```
