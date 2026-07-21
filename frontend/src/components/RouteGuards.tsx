import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import type { Role } from '../types'

export function ProtectedRoute({ role = 'MEMBER' }: { role?: Role }) {
  const auth = useAuth(); const location = useLocation()
  if (auth.status === 'checking') return <div className="page-loader" role="status"><span className="spinner" />Restoring your workspace…</div>
  if (auth.status === 'anonymous') return <Navigate to="/login" state={{ from: location.pathname }} replace />
  if (!auth.user?.roles.includes(role) && !auth.user?.roles.includes('ADMIN')) return <Navigate to="/unauthorized" replace />
  return <Outlet />
}

export function PublicOnlyRoute() {
  const auth = useAuth()
  if (auth.status === 'checking') return <div className="page-loader" role="status"><span className="spinner" />Checking your session…</div>
  return auth.status === 'authenticated' ? <Navigate to="/dashboard" replace /> : <Outlet />
}
