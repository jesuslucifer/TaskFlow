import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { tap } from 'rxjs';
import { ITasks, ITasksResponse } from '../interface/tasks.interface';

@Injectable({
  providedIn: 'root',
})
export class TasksService {
  baseApiUrl = 'http://localhost:8080/api/projects';
  http = inject(HttpClient);
  tasks = signal<ITasks[] | null>(null);
  constructor() {}
  createTask(formValue: ITasksResponse, projectId: number) {
    return this.http
      .post<ITasks>(`${this.baseApiUrl}/${projectId}/tasks/create`, formValue)
      .pipe(
        tap((newTasks: ITasks) => {
          const current = this.tasks();
          this.tasks.set(current ? [...current, newTasks] : [newTasks]);
        })
      );
  }
  getTasks(projectId: number) {
    return this.http
      .get<ITasks[]>(`${this.baseApiUrl}/${projectId}/tasks/all`)
      .pipe(
        tap((tasks: ITasks[]) => {
          this.tasks.set(tasks);
        })
      );
  }

  getTaskById(projectId: number, taskId: number) {
    return this.http.get<ITasks>(
      `${this.baseApiUrl}/${projectId}/tasks/${taskId}`
    );
  }
  deleteTask(projectId: number, taskId: number) {
    return this.http
      .delete(`${this.baseApiUrl}/${projectId}/tasks/${taskId}/delete`, {
        responseType: 'text',
      })
      .pipe(
        tap(() => {
          const current = this.tasks();
          if (current) {
            const updated = current.filter((p) => p.id !== taskId);
            this.tasks.set(updated);
          }
        })
      );
  }
  updateTask(data: ITasks, projectId: number, taskId: number) {
    return this.http.put(
      `${this.baseApiUrl}/${projectId}/tasks/${taskId}/update`,
      data
    );
  }
}
