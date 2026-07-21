import { screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import { renderApp } from '../test/renderApp'

describe('protected routing', () => {
  it('shows the bootstrap state and redirects an anonymous member to login', async () => {
    let reject!: () => void
    vi.stubGlobal('fetch', vi.fn(() => new Promise<Response>((_resolve, rejectPromise) => { reject = rejectPromise })))
    renderApp('/dashboard')
    expect(screen.getByRole('status')).toHaveTextContent('Restoring your workspace')
    reject(); expect(await screen.findByRole('heading', { name: 'Welcome back' })).toBeInTheDocument()
  })
  it('restores a refresh session and renders the dashboard shell', async () => {
    const user = { id: '11111111-1111-4111-8111-111111111111', email: 'member@example.com', displayName: 'Sam Member', roles: ['MEMBER'] }
    vi.stubGlobal('fetch', vi.fn().mockResolvedValueOnce(new Response(JSON.stringify({ accessToken: 'token', tokenType: 'Bearer', expiresIn: 600, user }), { status: 200 })).mockResolvedValueOnce(new Response(JSON.stringify({ user, continueLearning: [], featuredTopics: [], recentActivity: [] }), { status: 200 })))
    renderApp('/dashboard')
    expect(await screen.findByRole('heading', { name: /Good to see you, Sam/ })).toBeInTheDocument()
    expect(screen.getByRole('navigation', { name: 'Primary' })).toBeInTheDocument()
    expect(await screen.findByText('No activity yet')).toBeInTheDocument()
  })
})
