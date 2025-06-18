import { tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { IProject, IProjectResponse } from '../interface/project.interface';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  http = inject(HttpClient);
  baseApiUrl = 'http://localhost:8080/api/projects';
  projects = signal<IProject[] | null>(null);

  createProject(projectForm: IProjectResponse) {
    return this.http.post<IProject>(`${this.baseApiUrl}/create`, projectForm);
  }
  getAllProjects() {
    return this.http.get<IProject[]>(`${this.baseApiUrl}/`).pipe(
      tap((res: IProject[]) => {
        console.log(res);
        this.projects.set(res);
      })
    );
  }
  getAllUserProjects(userId: number) {
    return this.http
      .get<IProject[]>(`${this.baseApiUrl}/${userId}/creator`)
      .pipe(
        tap((res: IProject[]) => {
          console.log(res);
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
    return this.http.delete(`${this.baseApiUrl}/${id}`);
  }
  updateProject(projectId: number, projectForm: IProjectResponse) {
    return this.http.put(
      `${this.baseApiUrl}/projects/${projectId}`,
      projectForm
    );
  }
}
