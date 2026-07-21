import { screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, expect, it, vi } from 'vitest'
import { renderApp } from '../test/renderApp'

const user = { id: '11111111-1111-4111-8111-111111111111', email: 'learner@example.com', displayName: 'Alex Learner', roles: ['MEMBER'] }
describe('login experience', () => {
  it('shows accessible validation and a generic authentication error', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValueOnce(new Response('', { status: 401 })).mockResolvedValueOnce(new Response(JSON.stringify({ title: 'Unauthorized', detail: 'Bad credentials', status: 401 }), { status: 401, headers: { 'Content-Type': 'application/problem+json' } })))
    renderApp('/login')
    expect(await screen.findByRole('heading', { name: 'Welcome back' })).toBeInTheDocument()
    await userEvent.type(screen.getByLabelText('Email address'), 'learner@example.com')
    await userEvent.type(screen.getByLabelText('Password'), 'wrong')
    await userEvent.click(screen.getByRole('button', { name: 'Sign in' }))
    expect(await screen.findByRole('alert')).toHaveTextContent('We couldn’t sign you in')
  })
  it('logs in and lands on the dashboard', async () => {
    const session = { accessToken: 'token', tokenType: 'Bearer', expiresIn: 600, user }
    const dashboard = { user, continueLearning: [], featuredTopics: [], recentActivity: [] }
    vi.stubGlobal('fetch', vi.fn().mockResolvedValueOnce(new Response('', { status: 401 })).mockResolvedValueOnce(new Response(JSON.stringify(session), { status: 200 })).mockResolvedValueOnce(new Response(JSON.stringify(dashboard), { status: 200 })))
    renderApp('/login')
    await screen.findByRole('heading', { name: 'Welcome back' })
    await userEvent.type(screen.getByLabelText('Email address'), user.email)
    await userEvent.type(screen.getByLabelText('Password'), 'correct-password')
    await userEvent.click(screen.getByRole('button', { name: 'Sign in' }))
    expect(await screen.findByRole('heading', { name: /Good to see you, Alex/ })).toBeInTheDocument()
  })
})
