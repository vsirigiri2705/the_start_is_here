import { render } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { MemoryRouter } from 'react-router-dom'
import { AuthProvider } from '../auth/AuthContext'
import { ThemeProvider } from '../theme/ThemeContext'
import { App } from '../App'
export function renderApp(route: string) { return render(<QueryClientProvider client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}><ThemeProvider><MemoryRouter initialEntries={[route]}><AuthProvider><App/></AuthProvider></MemoryRouter></ThemeProvider></QueryClientProvider>) }
