export interface IUser {
  name: string;
  email: string;
  avatarUrl: string;
}

export interface IUserStats {
  activeProjects: number;
  completedProjects: number;
  overdueTasks: number;
  completedTasks: number;
}
