import { Component, inject, Input } from '@angular/core';
import {
  IProject,
  IProjectUpdate,
  Priority,
  Status,
} from '../../../../core/interface/project.interface';
import { CommonModule, DatePipe } from '@angular/common';
import { ProjectService } from '../../../../core/services/project.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-project-page-info',
  imports: [CommonModule, FormsModule],
  templateUrl: './project-page-info.component.html',
  styleUrl: './project-page-info.component.scss',
})
export class ProjectPageInfoComponent {
  @Input() project!: IProject;
  projectService = inject(ProjectService);

  priorityEnum = Priority;
  statusEnum = Status;

  editDateTo: string = '';
  correctDate: string | null = '';
  editTimeLeft: string = '';
  showEditDate = false;
  showEditTime = false;
  ngOnChanges() {
    this.editDateTo = this.project?.dateTo || '';
    this.editTimeLeft = this.project?.timeLeft || '';
  }
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
  cancelEditDate() {
    this.editDateTo = this.project.dateTo || '';
    this.showEditDate = false;
  }
  cancelEditTime() {
    this.editTimeLeft = this.project.timeLeft || '';
    this.showEditTime = false;
  }
  saveDateTo() {
    if (!this.editDateTo) return;
    this.correctDate = new DatePipe('en-US').transform(
      this.editDateTo,
      'dd-MM-yyyy'
    );
    this.projectService
      .patchProject(this.project.id, { dateTo: this.correctDate })
      .subscribe({
        next: () => {
          this.project.dateTo = this.editDateTo;
          this.showEditDate = false;
        },
        error: () => {},
      });
  }

  saveTimeLeft() {
    if (!this.editTimeLeft) return;

    this.projectService
      .patchProject(this.project.id, { timeLeft: this.editTimeLeft })
      .subscribe({
        next: () => {
          this.project.timeLeft = this.editTimeLeft;
          this.showEditTime = false;
        },
        error: () => {},
      });
  }
}
