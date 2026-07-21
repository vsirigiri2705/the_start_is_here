# Repository agent guidance

These instructions apply to Codex, Claude, and any other coding agent working in this repository.

## Before changing code

1. Read `docs/architecture/system-architecture.md`.
2. Read `docs/project/DECISIONS.md`.
3. Find the assigned work item in `docs/project/WORKBOARD.md` or its GitHub Issue.
4. Confirm the task has an exclusive file scope and acceptance criteria.

## Collaboration rules

- One task has one implementation owner.
- Do not edit files assigned to another active task.
- Shared contracts under `docs/contracts/` are changed only through a dedicated contract task.
- Codex owns backend, security, infrastructure, integration, and release verification by default.
- Claude owns frontend experience, accessibility, and user-facing documentation by default.
- The human owner approves product scope, external accounts, secrets, costs, and deployment.
- Never commit credentials, tokens, private keys, `.env` files, or real user information.
- Do not deploy, create paid resources, or change external permissions without human approval.

## Completion rules

Every implementation task must include:

- relevant automated tests;
- verification commands and their results;
- documentation for material behavior changes;
- a handoff based on `docs/project/HANDOFF_TEMPLATE.md`;
- known limitations and follow-up work.

Do not mark a task complete when required tests are failing or were not run.
