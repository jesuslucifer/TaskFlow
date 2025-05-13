export interface IProject {
  id: number;
  name: string;
  description: string;
  image?: string;
  tags: { tag: string }[];
  owner: string;
  createdAt: string;
  tasksCount: string;
  tasksCompleted: string;
}
