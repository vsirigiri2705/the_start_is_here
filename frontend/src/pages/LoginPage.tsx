import { Eye, EyeOff, Sparkles } from 'lucide-react'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { useLocation, useNavigate } from 'react-router-dom'
import { z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { useAuth } from '../auth/AuthContext'

const schema = z.object({ email: z.email('Enter a valid email address').max(254), password: z.string().min(1, 'Enter your password').max(1024) })
type Fields = z.infer<typeof schema>
export function LoginPage() {
  const [visible, setVisible] = useState(false); const [serverError, setServerError] = useState(''); const auth = useAuth(); const navigate = useNavigate(); const location = useLocation()
  const { register, handleSubmit, formState: { errors, isSubmitting, isValid } } = useForm<Fields>({ resolver: zodResolver(schema), mode: 'onChange', defaultValues: { email: '', password: '' } })
  const submit = async (fields: Fields) => { setServerError(''); try { await auth.login(fields.email, fields.password); const destination = typeof location.state?.from === 'string' && location.state.from.startsWith('/') && !location.state.from.startsWith('//') ? location.state.from : '/dashboard'; navigate(destination, { replace: true }) } catch { setServerError('We couldn’t sign you in. Check your details and try again.') } }
  return <main className="login-page">
    <section className="login-story" aria-label="Product introduction"><div className="brand light"><span className="brand-mark">S</span>The Start Is Here</div><div><span className="eyebrow"><Sparkles size={16}/> Your learning workspace</span><h1>Build knowledge.<br/>Make it yours.</h1><p>One focused place for the lessons, tools, and experiments that move you forward.</p></div><blockquote>“Every expert was once a beginner who kept going.”</blockquote></section>
    <section className="login-panel"><form className="login-card" onSubmit={handleSubmit(submit)} noValidate>
      <div><span className="mobile-brand">The Start Is Here</span><h2>Welcome back</h2><p>Sign in to continue your learning journey.</p></div>
      {serverError && <div className="alert" role="alert">{serverError}</div>}
      <div className="field"><label htmlFor="email">Email address</label><input id="email" type="email" autoComplete="email" aria-invalid={!!errors.email} aria-describedby={errors.email ? 'email-error' : undefined} placeholder="you@example.com" {...register('email')}/>{errors.email && <small id="email-error" className="field-error">{errors.email.message}</small>}</div>
      <div className="field"><label htmlFor="password">Password</label><div className="password-wrap"><input id="password" type={visible ? 'text' : 'password'} autoComplete="current-password" aria-invalid={!!errors.password} aria-describedby={errors.password ? 'password-error' : undefined} placeholder="Enter your password" {...register('password')}/><button type="button" aria-label={visible ? 'Hide password' : 'Show password'} onClick={() => setVisible(!visible)}>{visible ? <EyeOff/> : <Eye/>}</button></div>{errors.password && <small id="password-error" className="field-error">{errors.password.message}</small>}</div>
      <button className="button primary full" disabled={!isValid || isSubmitting}>{isSubmitting ? <><span className="spinner small"/>Signing in…</> : 'Sign in'}</button>
      <p className="invitation">Access is currently by invitation. Contact your administrator if you need an account.</p>
    </form></section>
  </main>
}
