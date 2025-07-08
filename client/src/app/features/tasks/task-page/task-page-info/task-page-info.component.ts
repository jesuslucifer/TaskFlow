import { Component, inject, Input } from '@angular/core';
import {
  IProject,
  Priority,
  Status,
} from '../../../../core/interface/project.interface';
import { ProjectService } from '../../../../core/services/project.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ITasks, TaskStatus } from '../../../../core/interface/tasks.interface';
import { TasksService } from '../../../../core/services/tasks.service';

@Component({
  selector: 'app-task-page-info',
  imports: [CommonModule, FormsModule],
  templateUrl: './task-page-info.component.html',
  styleUrl: './task-page-info.component.scss',
})
export class TaskPageInfoComponent {
  @Input() task!: ITasks;
  @Input() form: any;
  @Input() profileId!: number;

  projectService = inject(ProjectService);
  priorityEnum = Priority;
  statusEnum = TaskStatus;
  editDateTo: string = '';
  correctDate: string | null = '';
  editTimeLeft: string = '';
  showEditDate = false;
  showEditTime = false;

  patch(data: Partial<IProject>) {
    this.projectService.patchProject(this.task.id, data).subscribe({
      next: () => Object.assign(this.task, data),
      error: () => {},
    });
  }
  ngOnChanges() {
    this.editDateTo = this.task?.dateTo || '';
    this.editTimeLeft = this.task?.timeLeft || '';
  }
  onPriorityChange(newPriority: Priority) {
    this.form.value.priority = newPriority;
    this.patch({ ...this.form.value });
  }
  onStatusChange(newStatus: Status) {
    this.form.value.status = newStatus;
    this.patch({ ...this.form.value });
  }
  cancelEditDate() {
    this.editDateTo = this.task.dateTo || '';
    this.showEditDate = false;
  }
  cancelEditTime() {
    this.editTimeLeft = this.task.timeLeft || '';
    this.showEditTime = false;
  }
  saveDateTo() {
    if (!this.editDateTo) return;
    this.correctDate = new DatePipe('en-US').transform(
      this.editDateTo,
      'dd-MM-yyyy'
    );
    this.form.value.dateTo = this.correctDate;
    this.patch({ ...this.form.value });
    this.task.dateTo = this.editDateTo;
    this.showEditDate = false;
  }

  saveTimeLeft() {
    if (!this.editTimeLeft) return;
    this.form.value.timeLeft = this.editTimeLeft;
    this.patch({ ...this.form.value });
    this.task.timeLeft = this.editTimeLeft;
    this.showEditTime = false;
  }
}
