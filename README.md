# Ticketing System — Full project scaffold

This archive contains a full-stack scaffold for the Ticketing System assignment.

Folders:
- `backend/` — Spring Boot (Java 17, Maven). Minimal controllers, JPA entities and repositories. **NOTE:** Authentication is provided as a minimal placeholder (no JWT). Replace AuthController with JWT-based flow for production.
- `frontend/` — Next.js pages and components (minimal). Assumes backend API at `http://localhost:8080/api`.

## How to run

### Backend
1. Ensure Java 17 and Maven installed.
2. Create a PostgreSQL database, or adjust `backend/src/main/resources/application.yml` to point to your DB. Defaults use environment variables: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASS`.
3. From `backend/` run:
   ```bash
   mvn spring-boot:run
   ```
   On first run JPA will create tables.

### Frontend
1. Ensure Node 18+ installed.
2. From `frontend/` run:
   ```bash
   npm install
   npm run dev
   ```
3. Open http://localhost:3000

## Important notes & next steps
- This scaffold intentionally leaves **security (JWT, password hashing, role-based access)** as TODOs and uses naive plaintext password storage in `AuthController` for demo purposes. **Do not** use this code as-is in production.
- Recommended next steps:
  - Implement JWT generation/validation and secure endpoints with Spring Security.
  - Hash passwords (BCrypt).
  - Add email notifications, file attachments storage, and search/filter endpoints.
  - Improve frontend authentication and replace localStorage usage with secure cookies.

If you'd like, I can now:
- Implement full JWT-based auth + Spring Security and update frontend to store tokens.
- Or prepare a zip file with these files for download (already included below).


## Added features (by assistant)
- JWT-based authentication (register/login) using BCrypt and JWT tokens.
- Role-based access enforced: `/api/admin/**` requires ROLE_ADMIN; tickets & comments require authentication.
- EmailService using Spring Mail to send simple notifications on ticket creation/assignment. Configure SMTP via env vars.
- File upload/download endpoints at `/api/files/upload` and `/api/files/download/{filename}`. Files saved to `uploads/` in backend working dir.
- Search endpoint for tickets: `/api/tickets/search?subject=...&status=OPEN&priority=HIGH&assigneeId=2`.

**Security notice:** The scaffold is improved with JWT & BCrypt but still basic. For production, secure secrets, enable TLS, and validate inputs thoroughly.
