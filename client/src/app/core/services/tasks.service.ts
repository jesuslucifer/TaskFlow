import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { tap } from 'rxjs';
import {
  ISubtasks,
  ISubtaskUpdate,
  ITasks,
  ITasksResponse,
  ITaskUpdate,
} from '../interface/tasks.interface';

@Injectable({
  providedIn: 'root',
})
export class TasksService {
  baseApiUrl = 'http://localhost:8080/api/projects';
  http = inject(HttpClient);
  tasks = signal<ITasks[] | null>(null);
  subtasks = signal<ISubtasks[] | null>(null);

  constructor() {}
  createTask(formValue: ITasksResponse, projectId: number) {
    return this.http
      .post<ITasks>(`${this.baseApiUrl}/${projectId}/tasks/create`, formValue)
      .pipe(
        tap(() => {
          this.getTasks(projectId).subscribe();
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
          this.getTasks(projectId).subscribe();
        })
      );
  }
  updateTask(data: ITaskUpdate, projectId: number, taskId: number) {
    return this.http.put<ITasks>(
      `${this.baseApiUrl}/${projectId}/tasks/${taskId}/update`,
      data
    );
  }
  createSubtask(projectId: number, taskId: number, subtask: ISubtasks) {
    return this.http
      .post<ISubtasks>(
        `${this.baseApiUrl}/${projectId}/tasks/${taskId}/subtasks/create`,
        subtask
      )
      .pipe(
        tap(() => {
          this.getAllSubtasks(projectId, taskId).subscribe();
        })
      );
  }
  getAllSubtasks(projectId: number, taskId: number) {
    return this.http
      .get<ISubtasks[]>(
        `${this.baseApiUrl}/${projectId}/tasks/${taskId}/subtasks/all`
      )
      .pipe(
        tap((subtasks: ISubtasks[]) => {
          this.subtasks.set(subtasks);
        })
      );
  }
  updateSubtasks(
    projectId: number,
    taskId: number,
    subtaskId: number,
    updatedSubtask: ISubtaskUpdate
  ) {
    return this.http.put(
      `${this.baseApiUrl}/${projectId}/tasks/${taskId}/subtasks/${subtaskId}/update`,
      updatedSubtask
    );
  }
  deleteSubtask(projectId: number, taskId: number, subtaskId: number) {
    return this.http
      .delete<ISubtasks>(
        `${this.baseApiUrl}/${projectId}/tasks/${taskId}/subtasks/${subtaskId}/delete`
      )
      .pipe(
        tap(() => {
          this.getAllSubtasks(projectId, taskId).subscribe();
        })
      );
  }
}
