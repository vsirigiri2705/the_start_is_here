import { Navigate, Route, Routes } from 'react-router-dom'
import { AppShell } from './components/AppShell'
import { ProtectedRoute, PublicOnlyRoute } from './components/RouteGuards'
import { DashboardPage } from './pages/DashboardPage'
import { LoginPage } from './pages/LoginPage'
import { NotFoundPage, PlaceholderPage, UnauthorizedPage } from './pages/PlaceholderPage'

export function App() { return <Routes>
  <Route element={<PublicOnlyRoute/>}><Route path="/login" element={<LoginPage/>}/></Route>
  <Route path="/unauthorized" element={<UnauthorizedPage/>}/>
  <Route element={<ProtectedRoute/>}><Route element={<AppShell/>}>
    <Route path="/dashboard" element={<DashboardPage/>}/>
    <Route path="/topics" element={<PlaceholderPage title="Topics"/>}/><Route path="/topics/:topicSlug" element={<PlaceholderPage title="Topic overview"/>}/><Route path="/topics/:topicSlug/:lessonSlug" element={<PlaceholderPage title="Lesson"/>}/>
    <Route path="/tools" element={<PlaceholderPage title="Tools"/>}/><Route path="/bookmarks" element={<PlaceholderPage title="Bookmarks"/>}/><Route path="/profile" element={<PlaceholderPage title="Profile"/>}/>
    <Route path="*" element={<NotFoundPage/>}/>
  </Route></Route>
  <Route path="/" element={<Navigate to="/dashboard" replace/>}/>
  <Route path="*" element={<NotFoundPage/>}/>
</Routes> }
