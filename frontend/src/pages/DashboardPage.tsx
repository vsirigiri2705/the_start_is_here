import { ArrowRight, BookOpen, Clock3, Code2, Compass, Play, Sparkles, TerminalSquare } from 'lucide-react'
import { useQuery } from '@tanstack/react-query'
import { api } from '../api'
import { useAuth } from '../auth/AuthContext'

export function DashboardPage() {
  const { user, accessToken } = useAuth()
  const dashboard = useQuery({ queryKey: ['dashboard'], queryFn: () => api.dashboard(accessToken!), enabled: !!accessToken, retry: false })
  const data = dashboard.data
  return <div className="dashboard-page">
    <header className="page-heading"><div><span className="eyebrow"><Sparkles size={15}/> Learning workspace</span><h1>Good to see you, {user?.displayName.split(' ')[0]}.</h1><p>Pick up where you left off or explore something new.</p></div><button className="button primary"><Compass size={18}/>Explore topics</button></header>
    {dashboard.isPending && <div className="content-state" role="status"><span className="spinner"/>Loading your dashboard…</div>}
    {dashboard.isError && <div className="alert" role="alert">Your dashboard couldn’t load. <button onClick={() => void dashboard.refetch()}>Try again</button></div>}
    {!dashboard.isPending && !dashboard.isError && <>
      <section aria-labelledby="continue-title"><div className="section-heading"><div><p className="kicker">YOUR PROGRESS</p><h2 id="continue-title">Continue learning</h2></div></div>
        <div className="card-grid learning-grid">{data?.continueLearning.length ? data.continueLearning.map(card => <article className="card course-card" key={`${card.topicSlug}/${card.lessonSlug}`}><div className="course-icon"><BookOpen/></div><div><p className="kicker">{card.topicSlug.replaceAll('-', ' ')}</p><h3>{card.title}</h3></div><div className="progress-label"><span>{card.progressPercent}% complete</span></div><div className="progress"><span style={{ width: `${card.progressPercent}%` }}/></div><a href={`/topics/${card.topicSlug}/${card.lessonSlug}`}>Continue <ArrowRight size={17}/></a></article>) : <article className="card empty-card"><div className="course-icon"><Play/></div><h3>Start your first lesson</h3><p>Your active lessons and progress will appear here.</p></article>}</div>
      </section>
      <section aria-labelledby="topics-title"><div className="section-heading"><div><p className="kicker">DISCOVER</p><h2 id="topics-title">Featured topics</h2></div><a href="/topics">View all <ArrowRight size={16}/></a></div>
        <div className="card-grid topic-grid">{data?.featuredTopics.length ? data.featuredTopics.map((topic, i) => <a className="card topic-card" href={`/topics/${topic.slug}`} key={topic.slug}><span className={`topic-icon tone-${i % 3}`}>{i % 3 === 0 ? <Code2/> : i % 3 === 1 ? <TerminalSquare/> : <BookOpen/>}</span><h3>{topic.title}</h3><p>{topic.summary}</p><span className="card-link">Explore topic <ArrowRight size={16}/></span></a>) : <article className="card empty-card"><h3>Topics are on their way</h3><p>The learning catalog will appear here when content is published.</p></article>}</div>
      </section>
      <section aria-labelledby="activity-title"><div className="section-heading"><div><p className="kicker">LATEST</p><h2 id="activity-title">Recent activity</h2></div></div><div className="card activity-card">{data?.recentActivity.length ? <ul>{data.recentActivity.map(item => <li key={item.id}><Clock3/><span>{item.description}<small>{new Date(item.occurredAt).toLocaleDateString()}</small></span></li>)}</ul> : <div className="empty-inline"><Clock3/><span><strong>No activity yet</strong><small>Your completed lessons and milestones will show up here.</small></span></div>}</div></section>
    </>}
  </div>
}
