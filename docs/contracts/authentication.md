# Authentication contract

## Scope

Milestone 1 supports invitation-only member accounts, password login, short-lived JWT access tokens, rotating refresh tokens, logout, current-user lookup, and `MEMBER` / `ADMIN` authorization.

Registration, password reset, email verification, social login, MFA, and public account creation are deliberately deferred.

## Browser session model

- The access token is returned in the response body and retained only in React memory.
- The refresh token is an opaque, high-entropy value delivered as an `HttpOnly`, `Secure` cookie.
- The API persists only a one-way hash of each refresh token.
- Refresh rotates both tokens. Reuse of an already-rotated token revokes the affected token family.
- Closing or reloading the browser loses the access token; React silently calls the refresh endpoint to restore the session.
- The frontend never reads, logs, or stores the refresh token.

## Access token claims

| Claim | Meaning |
|---|---|
| `iss` | Configured Learning Hub issuer |
| `aud` | `learning-hub-api` |
| `sub` | Immutable user UUID |
| `iat` | Issue time |
| `exp` | Expiration time |
| `jti` | Unique token identifier |
| `roles` | Application roles such as `MEMBER` |

Access tokens should expire after 10 minutes by default. This is configurable by environment.

## Refresh cookie

| Property | Contract |
|---|---|
| Name | `learning_hub_refresh` |
| Path | `/api/v1/auth` |
| HttpOnly | `true` |
| Secure | `true` outside local development |
| SameSite | `Strict` when frontend and API share a site; otherwise explicitly reviewed |
| Lifetime | Seven days by default |

## Endpoints

### Login

`POST /api/v1/auth/login`

- Accepts email and password.
- Uses a generic `401` response for invalid email, invalid password, disabled account, or locked account.
- Returns the access token and current user summary.
- Sets the refresh cookie on success.

### Refresh

`POST /api/v1/auth/refresh`

- Reads the refresh cookie.
- Validates token hash, expiry, revocation state, user state, and token family.
- Rotates the refresh token transactionally.
- Returns a new access token and refresh cookie.

### Logout

`POST /api/v1/auth/logout`

- Revokes the presented refresh session when present.
- Clears the refresh cookie.
- Is idempotent and returns `204` even when no active session exists.

### Current user

`GET /api/v1/users/me`

- Requires a valid access token.
- Returns non-sensitive identity and role data.

## Authorization

- `/api/v1/auth/login`, `/refresh`, and `/logout` are accessible without an access token.
- `/api/v1/users/me`, dashboard, topics, progress, and bookmarks require `MEMBER` or `ADMIN`.
- Administration endpoints require `ADMIN`.
- Authorization is enforced by the API even when the frontend hides unavailable actions.

## Error behavior

All API errors use the shared problem format defined in `api.yaml`. Authentication failures must not reveal whether an account exists. Validation failures may identify invalid fields but must never echo passwords or tokens.

## Required security tests

- Valid and invalid login.
- Disabled and locked account behavior.
- Missing, expired, malformed, incorrectly signed, wrong-issuer, and wrong-audience JWTs.
- Member access to member endpoints.
- Member rejection from administrator endpoints.
- Successful refresh rotation.
- Expired, revoked, and reused refresh tokens.
- Logout and idempotent logout.
- CORS allow and reject behavior.
- No passwords or raw refresh tokens in persistence or logs.
