# Decision log

Record product and architecture decisions here before agents implement dependent work.

| ID | Decision | Status | Owner | Rationale |
|---|---|---|---|---|
| D-001 | Use a modular monolith with React and Spring Boot | Provisionally accepted | Human | Preserve clarity and low deployment cost while teaching boundaries |
| D-002 | Use PostgreSQL with Flyway migrations | Provisionally accepted | Human | Portable relational model and repeatable environments |
| D-003 | Use short-lived JWT access tokens and rotating refresh cookies | Provisionally accepted | Human | Teach JWT security without long-lived browser tokens |
| D-004 | Begin with invitation-only registration | Provisionally accepted | Human | Reduce account-recovery, abuse and email-verification scope in v0 |
| D-005 | Keep lesson content versioned as Markdown initially | Provisionally accepted | Human | Make content reviewable, portable and inexpensive to host |
| D-006 | Use GitHub Projects as the cross-agent control center | Provisionally accepted | Human | One vendor-neutral view of work, decisions, reviews and evidence |
| D-007 | Use an adaptive light and dark visual system | Provisionally accepted | Human | Respect member preference and teach theme-aware component design |

## Decision procedure

1. Add a proposed decision with alternatives and consequences when needed.
2. The human owner accepts, rejects, or requests an experiment.
3. Update the status to `Accepted`, `Rejected`, or `Superseded`.
4. Link the decision from dependent GitHub Issues and pull requests.
5. Material reversals receive a new decision rather than silently rewriting history.

## Open decisions

- Final application name and short description.
- Initial audience: personal, invited learners, or public members.
- Initial five learning topics.
- Whether the first AWS exercise uses Elastic Beanstalk or a container-first service.
