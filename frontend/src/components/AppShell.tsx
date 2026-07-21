import { BookOpen, Bookmark, LayoutDashboard, Menu, Moon, Sun, Wrench, X } from 'lucide-react'
import { useState } from 'react'
import { NavLink, Outlet } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { useTheme } from '../theme/ThemeContext'

const nav = [{ to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard }, { to: '/topics', label: 'Topics', icon: BookOpen }, { to: '/tools', label: 'Tools', icon: Wrench }, { to: '/bookmarks', label: 'Bookmarks', icon: Bookmark }]
export function AppShell() {
  const [open, setOpen] = useState(false); const { user, logout } = useAuth(); const { theme, setTheme } = useTheme()
  const sidebar = <>
    <div className="brand"><span className="brand-mark">S</span><span>The Start Is Here</span></div>
    <nav aria-label="Primary">{nav.map(({ to, label, icon: Icon }) => <NavLink key={to} to={to} onClick={() => setOpen(false)}><Icon size={19}/>{label}</NavLink>)}</nav>
    <div className="sidebar-bottom">
      <button className="nav-button" onClick={() => setTheme(theme === 'dark' ? 'light' : 'dark')}>{theme === 'dark' ? <Sun size={19}/> : <Moon size={19}/>}Theme</button>
      <div className="account"><span className="avatar">{user?.displayName.charAt(0).toUpperCase()}</span><span><strong>{user?.displayName}</strong><small>{user?.email}</small></span></div>
      <button className="button secondary full" onClick={() => void logout()}>Log out</button>
    </div>
  </>
  return <div className="app-shell">
    <a className="skip-link" href="#main">Skip to content</a>
    <aside className="sidebar">{sidebar}</aside>
    <header className="mobile-header"><div className="brand"><span className="brand-mark">S</span>The Start Is Here</div><button className="icon-button" aria-label={open ? 'Close navigation' : 'Open navigation'} aria-expanded={open} onClick={() => setOpen(!open)}>{open ? <X/> : <Menu/>}</button></header>
    {open && <div className="mobile-drawer">{sidebar}</div>}
    <main id="main"><Outlet /></main>
  </div>
}
