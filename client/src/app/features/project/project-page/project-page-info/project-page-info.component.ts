import { Component, inject, Input } from '@angular/core';
import {
  IProject,
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
  @Input() form: any;
  @Input() profileId!: number;

  projectService = inject(ProjectService);
  priorityEnum = Priority;
  statusEnum = Status;
  editDateTo: string = '';
  correctDate: string | null = '';
  editTimeLeft: string = '';
  showEditDate = false;
  showEditTime = false;

  patch(data: Partial<IProject>) {
    this.projectService.patchProject(this.project.id, data).subscribe({
      next: () => Object.assign(this.project, data),
      error: () => {},
    });
  }
  ngOnChanges() {
    this.editDateTo = this.project?.dateTo || '';
    this.editTimeLeft = this.project?.timeLeft || '';
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
    this.form.value.dateTo = this.correctDate;
    this.patch({ ...this.form.value });
    this.project.dateTo = this.editDateTo;
    this.showEditDate = false;
  }

  saveTimeLeft() {
    if (!this.editTimeLeft) return;
    this.form.value.timeLeft = this.editTimeLeft;
    this.patch({ ...this.form.value });
    this.project.timeLeft = this.editTimeLeft;
    this.showEditTime = false;
  }
}
