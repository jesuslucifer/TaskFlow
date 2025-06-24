import { Component, inject, Input, effect } from '@angular/core';
import {
  IProject,
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
    this.projectService
      .patchProject(this.project.id, { priority: newPriority })
      .subscribe({
        next: () => {
          this.project.priority = newPriority;
        },
        error: () => {},
      });
  }
  onStatusChange(newStatus: Status) {
    this.projectService
      .patchProject(this.project.id, { status: newStatus })
      .subscribe({
        next: () => {
          this.project.status = newStatus;
        },
        error: () => {},
      });
  }
  onDeleteProject() {
    this.projectService.deleteProject(this.project!.id).subscribe(() => {});
    console.log(this.project);
  }
}
