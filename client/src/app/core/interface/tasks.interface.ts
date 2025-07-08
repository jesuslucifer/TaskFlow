import { ICategory } from './project.interface';
import { IProfile } from './user.interface';

export enum TaskStatus {
  TODO = 'TODO',
  PROGRESS = 'PROGRESS',
  DONE = 'DONE',
}
export interface ITasks {
  id: number;
  name: string;
  description: string;
  status: string;
  priority: string;
  dateTo: string;
  timeLeft: string;
  createUser: IProfile;
  dateCreate: string;
  categories: ICategory[];
}
export interface ITasksResponse {
  name: string;
  description: string;
  status: string;
  priority: string;
  dateTo: string;
  timeLeft: string;
}
export interface ISubtasks {
  id: number;
  name: string;
  description: string;
  isDone: boolean;
}
export interface ISubtaskUpdate {
  id: number;
  name: string;
  description: string;
  isDone: boolean;
}
export interface ISubtasksResponse {
  name: string;
  description: string;
  status: string;
  priority: string;
}
export interface ITaskUpdate {
  name: string;
  description: string;
  status: string;
  priority: string;
  dateTo: string;
  timeLeft: string;
}
