# Learning Hub API

Java 21 / Spring Boot API for the authentication vertical slice.

## Run locally

1. Start PostgreSQL: `docker compose -f ../infrastructure/local/compose.yml up -d`.
2. Run: `mvn spring-boot:run`.
3. Health: `GET http://localhost:8080/actuator/health`.

If PostgreSQL was installed with Homebrew instead of Docker, create the configured local role and database once:

```sql
CREATE ROLE learning_hub LOGIN PASSWORD 'learning_hub_local';
CREATE DATABASE learning_hub OWNER learning_hub;
```

Then run the API normally with `mvn spring-boot:run`.

No account is seeded. Invitation provisioning is deliberately deferred; tests create users directly. Never use the development database password or JWT secret in a hosted environment. Set `DATABASE_*`, `JWT_SECRET`, `JWT_ISSUER`, `SECURE_COOKIE=true`, and `ALLOWED_ORIGINS` there.
