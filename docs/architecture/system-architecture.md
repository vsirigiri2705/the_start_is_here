# System architecture

## Context

The Start Is Here is a modular monolith with a React single-page application, a Spring Boot API, PostgreSQL persistence, and repository-managed learning content. It begins as a learning project and retains clear boundaries so features can grow without prematurely introducing microservices.

## System context

```mermaid
flowchart LR
    Member["Member"]
    Admin["Administrator"]
    Owner["Project owner"]
    Browser["React web application"]
    API["Spring Boot API"]
    DB[("PostgreSQL")]
    Content["Git-managed Markdown content"]
    AI["Optional AI provider / local model"]
    Hosting["Free hosting or AWS"]

    Member -->|"learn, search, track progress"| Browser
    Admin -->|"manage users and catalog"| Browser
    Owner -->|"approves releases and infrastructure"| Hosting
    Browser -->|"HTTPS JSON API"| API
    API -->|"users, sessions, progress, bookmarks"| DB
    API -->|"read indexed lessons"| Content
    API -.->|"future grounded questions"| AI
    Hosting --> Browser
    Hosting --> API
```

## Application containers

```mermaid
flowchart TB
    subgraph Client["React / TypeScript"]
        Router["Router and route guards"]
        AuthUI["Login and session UX"]
        Dashboard["Dashboard"]
        TopicsUI["Topics and lesson reader"]
        ToolsUI["Learning tools"]
        Query["TanStack Query API layer"]
        Router --> AuthUI
        Router --> Dashboard
        Router --> TopicsUI
        Router --> ToolsUI
        AuthUI --> Query
        Dashboard --> Query
        TopicsUI --> Query
    end

    subgraph Server["Spring Boot modular monolith"]
        Security["Spring Security filter chain"]
        Auth["Identity and session module"]
        Catalog["Topic catalog module"]
        Progress["Progress and bookmark module"]
        ToolRegistry["Tool registry module"]
        AdminAPI["Administration module"]
        AIAdapter["Future AI adapter"]
        Security --> Auth
        Security --> Catalog
        Security --> Progress
        Security --> ToolRegistry
        Security --> AdminAPI
        Catalog -.-> AIAdapter
    end

    subgraph Data["Data and content"]
        Users[("users / roles")]
        Sessions[("refresh sessions")]
        Learning[("progress / bookmarks")]
        Markdown["versioned Markdown lessons"]
        Vectors[("future vector index")]
    end

    Query -->|"Bearer access token + JSON"| Security
    Auth --> Users
    Auth --> Sessions
    Progress --> Learning
    Catalog --> Markdown
    AIAdapter -.-> Vectors
```

## Authentication flow

```mermaid
sequenceDiagram
    actor U as Member
    participant R as React
    participant S as Spring Security
    participant A as Auth service
    participant D as PostgreSQL

    U->>R: Submit email and password
    R->>S: POST /api/v1/auth/login
    S->>A: Authenticate credentials
    A->>D: Load user and password hash
    D-->>A: User, roles, status
    A->>A: Verify BCrypt hash
    A->>D: Store hashed refresh session
    A-->>R: Access token + secure refresh cookie
    R->>S: GET /api/v1/dashboard with Bearer token
    S->>S: Verify signature, issuer, audience and expiry
    S-->>R: Authorized dashboard data
    R->>S: POST /api/v1/auth/refresh after expiry
    S->>D: Validate and rotate refresh session
    S-->>R: New access token + rotated cookie
```

## Static and dynamic boundaries

| Capability | Static or versioned | Dynamic or persisted |
|---|---|---|
| Application shell and design system | Yes | No |
| Lesson Markdown and topic metadata | Yes initially | Admin editing may come later |
| Tool definitions | Yes initially | Usage history later |
| Users, password hashes and roles | No | Yes |
| Refresh sessions and revocation | No | Yes |
| Progress, bookmarks and recent activity | No | Yes |
| Search | Build-time index initially | Personalized search later |
| AI conversations and feedback | No | Future dynamic feature |

## Deployment topology

```mermaid
flowchart LR
    GitHub["GitHub repository"]
    Actions["GitHub Actions"]

    subgraph Free["Free learning environment"]
        CFP["Cloudflare Pages: React"]
        Render["Render: Spring Boot container"]
        Neon[("Neon PostgreSQL")]
        CFP -->|"HTTPS"| Render
        Render --> Neon
    end

    subgraph AWS["AWS practice environment"]
        CF["CloudFront"]
        S3["S3 static frontend"]
        EB["Elastic Beanstalk: Spring Boot"]
        RDS[("RDS PostgreSQL")]
        SM["Secrets Manager / Parameter Store"]
        CW["CloudWatch"]
        CF --> S3
        CF -->|"/api/*"| EB
        EB --> RDS
        SM --> EB
        EB --> CW
    end

    GitHub --> Actions
    Actions --> CFP
    Actions --> Render
    Actions -.->|"human-approved deployment"| S3
    Actions -.->|"human-approved deployment"| EB
```

## Security boundaries

- Access tokens are short-lived and retained in browser memory.
- Refresh tokens use `HttpOnly`, `Secure`, and appropriate `SameSite` cookies.
- Only hashes of refresh tokens are persisted.
- Passwords use an adaptive password hash such as BCrypt.
- The API validates JWT signature, issuer, audience, expiry, and authorization claims.
- CORS is restricted to configured frontend origins.
- Secrets enter deployments through environment or managed secret stores, never Git.
- Authentication, authorization, token rotation, replay, and logout receive integration tests.

## Evolution path

1. Authentication and protected dashboard.
2. Versioned learning catalog, progress, and bookmarks.
3. Administration and improved search.
4. Local-model experimentation with Ollama.
5. Grounded lesson assistant through a provider-neutral AI interface.
6. Vector search and evaluation only after content and privacy policies are established.
