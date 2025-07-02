export interface IProfile {
  id: number;
  username: string | null;
  email: string | null;
  avatarUrl: string | null;
}
export interface IUserStats {
  activeProjects: number;
  completedProjects: number;
  overdueTasks: number;
  completedTasks: number;
}
