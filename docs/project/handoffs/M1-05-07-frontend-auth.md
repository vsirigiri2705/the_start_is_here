# Agent handoff: M1-05–07 — frontend authentication and dashboard

## Owner and scope

- Implementing agent: frontend implementation agent (Claude-owned scope)
- GitHub Issue: #1
- Branch or worktree: `feature/frontend-auth` / `/private/tmp/the-start-frontend`
- Exclusive file scope: `frontend/` and this handoff

## Outcome

Members now have a modern, responsive React client with an accessible login flow, in-memory access tokens, silent refresh bootstrap, protected routing, adaptive light/dark theming, a mobile/desktop dashboard shell, contract-backed dashboard content, and informative loading, empty, and failure states.

## Files changed

- `frontend/src/api.ts` — API client matching `api.yaml`, including cookie credentials and bearer headers.
- `frontend/src/auth/` — session bootstrap, login, logout, and memory-only access-token state.
- `frontend/src/components/` — route guards and responsive application shell.
- `frontend/src/pages/` — login, dashboard, authorization, not-found, and milestone-two placeholders.
- `frontend/src/styles.css` — semantic adaptive design system and responsive layout.
- `frontend/src/**/*.test.tsx` — login and protected-routing component tests.
- `frontend/package.json` and tool configuration — Vite, TypeScript, ESLint, Vitest, React Query, React Hook Form, Zod, and Lucide.

## Decisions and assumptions

- Followed D-003 and kept the access token in React state only; refresh is cookie-managed and attempted at startup.
- Followed D-007 using a system-aware theme with an explicit user override stored in local storage.
- Dashboard content comes exclusively from `GET /dashboard`; no production-looking content is fabricated when lists are empty.
- Milestone-two routes have protected placeholders so navigation and routing contracts are testable without implementing out-of-scope features.

## Verification

- `npm run lint`: passed with zero errors (two Fast Refresh advisory warnings for context/hook co-location).
- `npm run typecheck`: passed.
- `npm test`: 2 files and 4 tests passed.
- `npm run build`: production build passed; 1,728 modules transformed.

## Security and privacy

- Access and refresh tokens are never persisted or logged by frontend code.
- API requests include refresh cookies through `credentials: include` and send bearer tokens only to the configured API base URL.
- Post-login destinations accept only internal absolute paths and reject protocol-relative destinations.
- Authentication errors remain generic and do not reveal account status.
- `npm audit` reported zero known vulnerabilities at installation time.

## Known limitations

- Automatic access-token renewal after an authenticated API call returns 401 is deferred to M1-08 integration; startup refresh is implemented.
- Account menu behavior is represented through direct controls rather than a popup menu.
- Browser-level accessibility automation and Playwright end-to-end testing belong to M1-08/M1-09; component tests cover the key route and form states.
- The Google Fonts import has a system-font fallback but should be self-hosted or removed if a strict production content-security policy forbids it.

## Next owner

Codex integration can merge this branch after backend M1-01–04 and verify real cookies, CORS, response fields, refresh/logout behavior, and end-to-end navigation. Shared contracts and backend files were not changed.
