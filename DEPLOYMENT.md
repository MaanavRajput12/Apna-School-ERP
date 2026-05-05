# Deployment

## Local containers

1. Copy `.env.example` to `.env` and adjust values if needed.
2. Run:

```bash
docker compose up --build
```

## URLs

- Frontend: `http://localhost:4000`
- Backend: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Notes

- The Angular SSR server proxies backend routes to the Spring Boot service through `BACKEND_URL`.
- Backend CORS origins are configured with `APP_CORS_ALLOWED_ORIGINS`.
- PostgreSQL data is persisted in the `postgres-data` Docker volume.
