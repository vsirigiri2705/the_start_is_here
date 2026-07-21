# Agent handoff: M1-01–04 — backend authentication vertical slice

## Owner and scope

- Implementing agent: Codex backend agent
- GitHub Issue: #4
- Branch or worktree: `feature/backend-auth`, `/private/tmp/the-start-backend`
- Exclusive file scope: `backend/`, `infrastructure/local/`, this handoff

## Outcome

The Spring Boot API now implements BCrypt login for active invited users, short-lived JWT access tokens, opaque refresh cookies, hashed refresh-session persistence, transactional token rotation, replay-family revocation, idempotent logout, current-user lookup, and a protected dashboard response matching the contract. PostgreSQL starts through the local Compose definition and Flyway creates the identity/session schema.

## Files changed

- `backend/pom.xml`: Java 21 Spring Boot build and test dependencies.
- `backend/src/main/java/dev/thestartishere/`: API, identity, session, and security modules.
- `backend/src/main/resources/db/migration/V1__identity_and_sessions.sql`: append-only initial schema.
- `backend/src/test/`: credential, token-claim/signature, rotation replay, and logout tests.
- `infrastructure/local/compose.yml`: PostgreSQL 16 local service and health check.

## Decisions and assumptions

- Implements D-001 through D-004 and the accepted API/auth/data contracts.
- Uses HS256 with a configurable 32+ byte deployment secret for the first learning slice. Moving to asymmetric keys can be a focused follow-up without changing the API.
- Dashboard catalog cards are versioned static starter data; personal sections are empty until Milestone 2.

## Verification

- `mvn -Dmaven.repo.local=/private/tmp/m2-learning-hub test`: BUILD SUCCESS; 6 tests, 0 failures, 0 errors.
- Compilation targets Java 21. Verification ran on the available Java 26 runtime, with Byte Buddy's experimental compatibility flag.

## Security and privacy

- Password verification uses BCrypt and authentication errors are intentionally generic.
- Only SHA-256 refresh-token hashes are persisted; raw refresh tokens only enter an HttpOnly cookie.
- Refresh lookup is pessimistically locked. Replay revocation is committed even though the request returns an authentication failure.
- Hosted environments must replace the local JWT/database secrets, enable secure cookies, and restrict allowed origins.

## Known limitations

- No invitation/account provisioning command or seed user is included yet; integration tests or an administrator workflow must create users.
- PostgreSQL/Testcontainers migration and HTTP integration tests remain desirable; current automated coverage is service/token focused.
- Authentication rate limiting, account recovery, asymmetric signing keys, and refresh-session cleanup are deferred.
- Cross-site free hosting may require an explicit SameSite/CORS design review before deployment.

## Next owner

Codex integration can merge this commit after review, add a safe local invitation mechanism, and run browser integration against the frontend. Do not alter shared contracts to accommodate implementation details without a contract task.
