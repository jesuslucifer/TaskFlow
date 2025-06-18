import { IProfile } from './user.interface';

export interface IProjectResponse {
  name: string;
  description: string;
  dateTo: string;
  timeLeft: string;
  priority: string;
}
export interface IExecutors {
  user: IProfile;
  role: string;
}
export interface IProject {
  id: number;
  name: string;
  description: string;
  createUser: IProfile;
  dateTo: string;
  timeLeft: string;
  status: string;
  priority: string;
  category: string;
  executors: IExecutors;
}
