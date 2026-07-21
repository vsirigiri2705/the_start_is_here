# Design contract

## Direction

The interface is calm, modern, content-first, and adaptive to light and dark color preferences. It should feel like a focused personal workspace rather than a marketing site or dense enterprise console.

## Principles

1. Clarity before decoration.
2. One clear primary action per surface.
3. Reusable primitives before page-specific styling.
4. Complete loading, empty, error, disabled, focus, hover, and success states.
5. Keyboard operation and visible focus are required.
6. Motion supports orientation and respects reduced-motion preferences.

## Semantic tokens

Implementation uses CSS custom properties surfaced through Tailwind. Components consume semantic names rather than raw palette values.

| Token | Purpose |
|---|---|
| `background` | Application background |
| `foreground` | Primary text |
| `surface` | Sidebar and page sections |
| `card` | Interactive cards |
| `muted` | Subtle backgrounds |
| `muted-foreground` | Secondary text |
| `primary` | Primary actions and active navigation |
| `primary-foreground` | Content on primary surfaces |
| `border` | Structural dividers |
| `input` | Form-control boundary |
| `ring` | Keyboard focus |
| `success` | Completed or successful state |
| `warning` | Attention without failure |
| `destructive` | Errors and destructive actions |

## Shape and spacing

- Use a four-pixel spacing base with a restrained set of multiples.
- Controls have comfortable touch targets of at least 44 by 44 CSS pixels where practical.
- Cards use one consistent medium radius; inputs and buttons use a slightly smaller radius.
- Shadows are subtle and reserved for elevation, not every container.
- Content widths favor reading comfort; dashboards may use the available width.

## Typography

- Use a high-quality system sans-serif stack initially to avoid a font-loading dependency.
- Preserve a clear hierarchy for page title, section title, card title, body, and metadata.
- Body text should remain at least 16 CSS pixels under default browser scaling.
- Do not use color alone to convey status.

## Required components for Milestone 1

- Button variants: primary, secondary, ghost, destructive.
- Text input, password input, label, field message.
- Alert and inline error.
- Loading indicator and page-loading state.
- Card and dashboard card.
- Avatar or initial fallback.
- Desktop sidebar and compact navigation.
- Theme control.
- Accessible menu for account actions.

## Login experience

- Focused form with product identity and a concise explanation.
- Email and password fields with autocomplete semantics.
- Password visibility control with an accessible name.
- Submit is disabled only while a request is active or required input is absent.
- Invalid credentials produce one generic form-level message.
- Field validation does not expose server authentication details.

## Dashboard experience

- Greeting and concise orientation.
- Continue-learning section.
- Topic and tool entry points.
- Recent activity and progress are allowed to be informative empty states in Milestone 1.
- Cards must remain usable from compact mobile widths through large desktop widths.

## Accessibility acceptance

- Meets WCAG 2.2 AA contrast and interaction expectations.
- All actions are keyboard reachable.
- Focus order follows reading order.
- Inputs have persistent labels and associated errors.
- Loading and form result changes are announced appropriately.
- Automated accessibility checks run for login and dashboard; critical flows also receive manual keyboard review.
