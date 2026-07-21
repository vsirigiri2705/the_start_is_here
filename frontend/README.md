# Frontend

React and TypeScript client for The Start Is Here.

## Local development

1. Copy `.env.example` to `.env.local` only when the API does not use the default `/api/v1` path.
2. Run `npm install`.
3. Run `npm run dev` while the API is available at `http://localhost:8080`.

Vite proxies `/api` requests in local development. Access tokens remain in React memory; refresh tokens are browser-managed `HttpOnly` cookies.

## Quality checks

- `npm run lint`
- `npm run typecheck`
- `npm test`
- `npm run build`
