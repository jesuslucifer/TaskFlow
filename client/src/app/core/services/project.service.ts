import { tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import {
  ICategory,
  IExecutors,
  IExecutorsResponse,
  IProject,
  IProjectUpdate,
  ProjectFilter,
} from '../interface/project.interface';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  http = inject(HttpClient);
  baseApiUrl = 'http://localhost:8080/api/projects';

  projects = signal<IProject[] | null>(null);
  executorsProject = signal<IProject[] | null>(null);
  createProject(projectForm: IProject) {
    return this.http.post<IProject>(`${this.baseApiUrl}/create`, projectForm);
  }

  getAllUserProjects() {
    return this.http
      .get<IProject[]>(`${this.baseApiUrl}`)

      .pipe(
        tap((res: IProject[]) => {
          this.projects.set(res);
        })
      );
  }
  getProjectByName(username: string, project_name: string) {
    return this.http.get<IProject>(
      `${this.baseApiUrl}/${username}/${project_name}`
    );
  }
  deleteProject(id: number) {
    return this.http.delete(`${this.baseApiUrl}/${id}`).pipe(
      tap(() => {
        const current = this.projects();
        if (current) {
          const updated = current.filter((p) => p.id !== id);
          this.projects.set(updated);
        }
      })
    );
  }
  patchProject(projectId: number, projectForm: IProjectUpdate) {
    return this.http.put<IProjectUpdate>(
      `${this.baseApiUrl}/${projectId}`,
      projectForm
    );
  }
  addExecutor(projectId: number, executor: IExecutorsResponse) {
    return this.http.put<IExecutorsResponse>(
      `${this.baseApiUrl}/${projectId}/executors/`,
      executor
    );
  }
  addCategory(projectId: number, category: ICategory) {
    return this.http.put<ICategory>(
      `${this.baseApiUrl}/${projectId}/category/`,
      category
    );
  }
  getProjectExecutors() {
    return this.http.get<IProject[]>(`${this.baseApiUrl}?role=executor`).pipe(
      tap((res: IProject[]) => {
        this.executorsProject.set(res);
      })
    );
  }
  deleteExecutor(projectId: number, executorId: number) {
    return this.http.delete<IExecutors>(
      `${this.baseApiUrl}/${projectId}/${executorId}/executors`
    );
  }

  findProjectsByName(name: string) {
    return this.http
      .get<IProject[]>(`${this.baseApiUrl}?name=${name}`)

      .pipe(
        tap((res: IProject[]) => {
          this.projects.set(res);
        })
      );
  }

  filterProjects(filter: ProjectFilter, forExecutors: boolean = false) {
    const params = new URLSearchParams();

    if (filter.name) params.append('name', filter.name);
    if (filter.status) params.append('status', filter.status);
    if (filter.priority) params.append('priority', filter.priority);
    if (filter.sortBy && filter.sortOrder) {
      params.append('sort', `${filter.sortBy},${filter.sortOrder}`);
    }

    if (forExecutors) {
      params.append('role', 'executor');
    }

    return this.http
      .get<IProject[]>(`${this.baseApiUrl}?${params.toString()}`)
      .pipe(
        tap((res) => {
          if (forExecutors) {
            this.executorsProject.set(res);
          } else {
            this.projects.set(res);
          }
        })
      );
  }
}
