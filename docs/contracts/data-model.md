# Data model contract

## Milestone 1 entities

```mermaid
erDiagram
    USER ||--o{ USER_ROLE : has
    ROLE ||--o{ USER_ROLE : grants
    USER ||--o{ REFRESH_SESSION : owns

    USER {
        uuid id PK
        varchar email UK
        varchar display_name
        varchar password_hash
        varchar status
        timestamptz created_at
        timestamptz updated_at
        bigint version
    }

    ROLE {
        smallint id PK
        varchar name UK
    }

    USER_ROLE {
        uuid user_id FK
        smallint role_id FK
    }

    REFRESH_SESSION {
        uuid id PK
        uuid user_id FK
        uuid family_id
        varchar token_hash UK
        timestamptz expires_at
        timestamptz rotated_at
        timestamptz revoked_at
        uuid replaced_by_session_id
        varchar revoke_reason
        timestamptz created_at
    }
```

## Constraints

- Normalize email for comparison and enforce uniqueness at the database boundary.
- User status is one of `INVITED`, `ACTIVE`, `LOCKED`, or `DISABLED`.
- Role name is one of `MEMBER` or `ADMIN` for v0.
- `USER_ROLE` has a composite primary key.
- Refresh expiry must be later than creation.
- A rotated session points to its replacement and cannot be used again.
- Application timestamps use UTC instants; presentation uses the member's locale later.
- Optimistic versioning protects user updates.

## Milestone 2 entities

```mermaid
erDiagram
    TOPIC ||--o{ LESSON : contains
    USER ||--o{ LESSON_PROGRESS : records
    LESSON ||--o{ LESSON_PROGRESS : receives
    USER ||--o{ BOOKMARK : creates
    LESSON ||--o{ BOOKMARK : targets

    TOPIC {
        uuid id PK
        varchar slug UK
        varchar title
        text summary
        int display_order
        boolean published
    }

    LESSON {
        uuid id PK
        uuid topic_id FK
        varchar slug
        varchar title
        varchar content_path UK
        int display_order
        boolean published
    }

    LESSON_PROGRESS {
        uuid user_id FK
        uuid lesson_id FK
        varchar state
        smallint percent_complete
        timestamptz last_viewed_at
        timestamptz completed_at
    }

    BOOKMARK {
        uuid id PK
        uuid user_id FK
        uuid lesson_id FK
        timestamptz created_at
    }
```

Lesson bodies remain Markdown files. Database records provide stable identifiers, ordering, publication state, and relationships to personal data.

## Migration policy

- Flyway migrations are append-only after merging.
- Schema changes and seed changes are separate migrations.
- Development seed users never contain production credentials.
- Integration tests start from an empty PostgreSQL database and apply the complete migration chain.
