import { Component, inject, Input } from '@angular/core';
import {
  IProject,
  IProjectUpdate,
  Priority,
  Status,
} from '../../../core/interface/project.interface';
import { MatIcon } from '@angular/material/icon';
import { ProjectService } from '../../../core/services/project.service';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
@Component({
  selector: 'app-project-card',
  imports: [
    MatIcon,
    MatButtonModule,
    MatMenuModule,
    RouterLink,
    CommonModule,
    FormsModule,
  ],
  templateUrl: './project-card.component.html',
  styleUrl: './project-card.component.scss',
})
export class ProjectCardComponent {
  @Input() project!: IProject;
  projectService = inject(ProjectService);
  priorityEnum = Priority;
  statusEnum = Status;
  onPriorityChange(newPriority: Priority) {
    const updatedProject: IProjectUpdate = {
      name: this.project.name,
      description: this.project.description,
      dateTo: this.project.dateTo,
      timeLeft: this.project.timeLeft,
      status: this.project.status,
      categories: this.project.categories,
      priority: newPriority,
    };
    this.patch(updatedProject);
  }

  onStatusChange(newStatus: Status) {
    const updatedProject: IProjectUpdate = {
      name: this.project.name,
      description: this.project.description,
      dateTo: this.project.dateTo,
      timeLeft: this.project.timeLeft,
      categories: this.project.categories,
      priority: this.project.priority,
      status: newStatus,
    };
    this.patch(updatedProject);
  }

  patch(updatedData: IProjectUpdate) {
    this.projectService.patchProject(this.project.id, updatedData).subscribe({
      next: () => {
        Object.assign(this.project, updatedData);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Ошибка при обновлении проекта:', err);
      },
    });
  }

  onDeleteProject() {
    this.projectService.deleteProject(this.project!.id).subscribe(() => {});
    console.log(this.project);
  }
}
