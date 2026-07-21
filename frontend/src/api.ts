import type { DashboardResponse, Problem, SessionResponse, UserSummary } from './types'

const baseUrl = import.meta.env.VITE_API_BASE_URL ?? '/api/v1'

export class ApiError extends Error {
  constructor(public status: number, public problem?: Problem) { super(problem?.detail ?? 'Something went wrong') }
}

async function request<T>(path: string, init: RequestInit = {}, accessToken?: string): Promise<T> {
  const response = await fetch(`${baseUrl}${path}`, {
    ...init,
    credentials: 'include',
    headers: { 'Content-Type': 'application/json', ...init.headers, ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}) },
  })
  if (!response.ok) {
    let problem: Problem | undefined
    try { problem = await response.json() as Problem } catch { /* non-JSON gateway response */ }
    throw new ApiError(response.status, problem)
  }
  if (response.status === 204) return undefined as T
  return response.json() as Promise<T>
}

export const api = {
  login: (email: string, password: string) => request<SessionResponse>('/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) }),
  refresh: () => request<SessionResponse>('/auth/refresh', { method: 'POST' }),
  logout: () => request<void>('/auth/logout', { method: 'POST' }),
  currentUser: (token: string) => request<UserSummary>('/users/me', {}, token),
  dashboard: (token: string) => request<DashboardResponse>('/dashboard', {}, token),
}
