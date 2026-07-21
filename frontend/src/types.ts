export type Role = 'MEMBER' | 'ADMIN'
export interface UserSummary { id: string; email: string; displayName: string; roles: Role[] }
export interface SessionResponse { accessToken: string; tokenType: 'Bearer'; expiresIn: number; user: UserSummary }
export interface LearningCard { topicSlug: string; lessonSlug: string; title: string; progressPercent: number }
export interface TopicCard { slug: string; title: string; summary: string }
export interface ActivityItem { id: string; description: string; occurredAt: string }
export interface DashboardResponse { user: UserSummary; continueLearning: LearningCard[]; featuredTopics: TopicCard[]; recentActivity: ActivityItem[] }
export interface Problem { title: string; status: number; detail: string; traceId?: string; fieldErrors?: Record<string, string[]> }
