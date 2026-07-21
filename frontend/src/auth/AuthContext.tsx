import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'
import { api } from '../api'
import type { UserSummary } from '../types'

type Status = 'checking' | 'authenticated' | 'anonymous'
interface AuthValue { status: Status; user: UserSummary | null; accessToken: string | null; login(email: string, password: string): Promise<void>; logout(): Promise<void> }
const AuthContext = createContext<AuthValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [status, setStatus] = useState<Status>('checking')
  const [user, setUser] = useState<UserSummary | null>(null)
  const [accessToken, setAccessToken] = useState<string | null>(null)

  const acceptSession = useCallback((session: Awaited<ReturnType<typeof api.refresh>>) => {
    setAccessToken(session.accessToken)
    setUser(session.user)
    setStatus('authenticated')
  }, [])

  useEffect(() => {
    let active = true
    api.refresh().then((session) => { if (active) acceptSession(session) }).catch(() => { if (active) setStatus('anonymous') })
    return () => { active = false }
  }, [acceptSession])

  const value = useMemo<AuthValue>(() => ({
    status,
    user,
    accessToken,
    login: async (email, password) => acceptSession(await api.login(email, password)),
    logout: async () => { try { await api.logout() } finally { setAccessToken(null); setUser(null); setStatus('anonymous') } },
  }), [acceptSession, accessToken, status, user])
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const value = useContext(AuthContext)
  if (!value) throw new Error('useAuth must be used inside AuthProvider')
  return value
}
