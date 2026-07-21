# Route contract

## Public routes

| Route | Purpose | Session behavior |
|---|---|---|
| `/login` | Member login | Authenticated users go to `/dashboard` |
| `/unauthorized` | Explain insufficient permission | May render with or without a session |
| `*` | Not-found experience | Preserves the application shell when authenticated |

## Protected routes

| Route | Required role | Milestone | Purpose |
|---|---|---|---|
| `/dashboard` | `MEMBER` | 1 | Overview, recent activity, topic and tool entry points |
| `/topics` | `MEMBER` | 2 | Browse and search learning topics |
| `/topics/:topicSlug` | `MEMBER` | 2 | Topic overview and lesson list |
| `/topics/:topicSlug/:lessonSlug` | `MEMBER` | 2 | Read a lesson and update progress |
| `/bookmarks` | `MEMBER` | 2 | Saved lessons and tools |
| `/tools` | `MEMBER` | 2 | Browse learning utilities |
| `/profile` | `MEMBER` | 2 | View account and preferences |
| `/admin` | `ADMIN` | Later | Administration entry point |

## Route guard states

The application session bootstrap has four explicit states:

1. `checking` — render a neutral application loading state while attempting refresh.
2. `authenticated` — render the requested protected route.
3. `anonymous` — redirect protected routes to `/login` and retain the intended destination.
4. `forbidden` — route authenticated users without the required role to `/unauthorized`.

After successful login, use the retained safe internal destination or `/dashboard`. Never redirect to an arbitrary external URL supplied through query parameters.

## Navigation contract

- Primary member navigation: Dashboard, Topics, Tools, Bookmarks.
- Secondary navigation: Profile, Theme, Logout.
- Administrator navigation appears only for `ADMIN`, but backend authorization remains authoritative.
- Desktop uses a persistent sidebar; compact widths use an accessible disclosure menu.
- The current route is visibly and programmatically identified.
