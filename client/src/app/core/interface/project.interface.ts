import { IProfile } from './user.interface';

export interface IProjectResponse {
  name: string;
  description: string;
  dateTo: string;
  timeLeft: string;
  priority: string;
  categories: ICategory[];
}

export interface IExecutorsResponse {
  executorId: number;
  executorRole: string;
}
export interface IExecutors {
  user: IProfile;
  role: string;
}
export interface ICategory {
  name: string;
}
export interface IProject {
  id: number;
  name: string;
  description: string;
  dateCreate: string;
  createUser: IProfile;
  dateTo: string;
  timeLeft: string;
  status: string;
  priority: string;
  categories: ICategory[];
  executors: IExecutors[];
}
export interface IProjectUpdate {
  name?: string;
  description?: string;
  dateTo?: string | null;
  timeLeft?: string;
  status?: string;
  priority?: string;
  categories?: ICategory[];
}
export enum Priority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL',
}
export enum Status {
  ACTIVE = 'ACTIVE',
  STOPPED = 'STOPPED',
  COMPLETED = 'COMPLETED',
  DRAFT = 'DRAFT',
}
export interface IProjectPut {
  form: IProjectUpdate;
}
