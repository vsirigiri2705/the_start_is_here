# Delivery workboard

The live dashboard should be a GitHub Project. This file is the repository-owned baseline and fallback when an agent cannot access the GitHub Project directly.

## Status model

`Backlog → Ready → In progress → In review → Human approval → Done`

## Ownership labels

- `owner:human`
- `owner:codex`
- `owner:claude`
- `area:architecture`
- `area:frontend`
- `area:backend`
- `area:security`
- `area:infrastructure`
- `area:documentation`
- `decision-needed`
- `blocked`

## Milestone 0 — Foundation

| ID | Work item | Owner | Depends on | Status | Acceptance evidence |
|---|---|---|---|---|---|
| M0-01 | Approve name, audience, registration policy and initial topics | Human | — | Ready | Decision log entries accepted |
| M0-02 | Define system architecture and boundaries | Codex | — | In review | Architecture document renders correctly |
| M0-03 | Define agent rules, workboard and handoff format | Codex | — | In review | Agent files present and consistent |
| M0-04 | Create GitHub Project, labels and milestones | Human + Codex | M0-03 | Ready | Project board linked from README |
| M0-05 | Define API, authentication, route and design contracts | Codex | M0-01 | Backlog | Contracts reviewed by Claude and human |

## Milestone 1 — Authentication vertical slice

| ID | Work item | Owner | Exclusive scope | Depends on | Acceptance evidence |
|---|---|---|---|---|---|
| M1-01 | Scaffold Spring Boot and module boundaries | Codex | `backend/` | M0-05 | Application starts; architecture test passes |
| M1-02 | Add PostgreSQL, Flyway and local containers | Codex | `backend/`, `infrastructure/local/` | M1-01 | Clean migration and health check |
| M1-03 | Implement users, roles and password authentication | Codex | `backend/` | M1-02 | Unit and integration tests |
| M1-04 | Implement JWT access and refresh-token rotation | Codex | `backend/` | M1-03 | Login, refresh, replay and logout tests |
| M1-05 | Scaffold React design system and routing | Claude | `frontend/` | M0-05 | Type check and component tests |
| M1-06 | Implement accessible login experience | Claude | `frontend/` | M1-05 | Responsive states and tests |
| M1-07 | Implement protected dashboard shell | Claude | `frontend/` | M1-05 | Route guard, responsive navigation, tests |
| M1-08 | Integrate authentication end to end | Codex | contract-approved cross-stack files | M1-04, M1-06, M1-07 | Browser test covers login through logout |
| M1-09 | Security and UX review | Claude reviews UX; Codex reviews security | Review only | M1-08 | Findings resolved or recorded |
| M1-10 | Accept milestone | Human | — | M1-09 | Manual acceptance checklist signed off |

## Milestone 2 — Learning dashboard

| ID | Work item | Owner | Depends on | Status |
|---|---|---|---|---|
| M2-01 | Define topic, lesson, progress and bookmark model | Codex | M1-10 | Backlog |
| M2-02 | Implement catalog and progress APIs | Codex | M2-01 | Backlog |
| M2-03 | Implement dashboard cards, search and filters | Claude | M2-01 | Backlog |
| M2-04 | Implement lesson reader and progress UX | Claude | M2-01 | Backlog |
| M2-05 | Integrate and run end-to-end learning flow | Codex | M2-02, M2-03, M2-04 | Backlog |

## Milestone 3 — Deployment

| ID | Work item | Owner | Depends on | Status |
|---|---|---|---|---|
| M3-01 | Add CI quality gates | Codex | M1-10 | Backlog |
| M3-02 | Create free-hosting definitions | Codex | M3-01 | Backlog |
| M3-03 | Create hosting accounts and secrets | Human | M3-02 | Backlog |
| M3-04 | Deploy free environment | Codex + Human approval | M3-03 | Backlog |
| M3-05 | Define AWS infrastructure and budget guardrails | Codex | M3-01 | Backlog |
| M3-06 | Create AWS resources and credentials | Human | M3-05 | Backlog |
| M3-07 | Deploy and verify AWS practice environment | Codex + Human approval | M3-06 | Backlog |

## Parallel-work rule

Backend tasks M1-01 through M1-04 can run in parallel with frontend tasks M1-05 through M1-07 only after M0-05 contracts are approved. M1-08 is the integration gate. No agent changes a shared contract during parallel implementation without a separate decision and review.
