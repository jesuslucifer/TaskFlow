import { Component, inject, Input, OnChanges } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  ITasks,
  ITaskUpdate,
  TaskStatus,
} from '../../../../core/interface/tasks.interface';
import { TasksService } from '../../../../core/services/tasks.service';
import {
  ICategory,
  Priority,
} from '../../../../core/interface/project.interface';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-task-page-info',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './task-page-info.component.html',
  styleUrl: './task-page-info.component.scss',
})
export class TaskPageInfoComponent implements OnChanges {
  @Input() task!: ITasks;
  @Input() form: any;
  @Input() profileId!: number;
  @Input() projectId!: number;
  toastr = inject(ToastrService);
  tasksService = inject(TasksService);
  showAddCategory = false;

  priorityEnum = Priority;
  statusEnum = TaskStatus;

  editDateTo: string = '';
  editTimeLeft: string = '';
  showEditDate = false;
  showEditTime = false;
  newCategoryInput = new FormControl<string>(''); // поле ввода

  ngOnChanges(): void {
    this.editDateTo = this.task?.dateTo || '';
    this.editTimeLeft = this.task?.timeLeft || '';
  }

  patch(data: ITaskUpdate) {
    this.tasksService.updateTask(data, this.projectId, this.task.id).subscribe({
      next: () => {
        Object.assign(this.task, data);
        this.toastr.success('Данные изменены');
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(err.error.message);
      },
    });
  }
  addCategory() {
    const name = this.newCategoryInput.value?.trim();
    if (!name) return;

    const newCategory: ICategory = { name: name };

    this.tasksService
      .addCategory(this.projectId, this.task.id, newCategory)
      .subscribe({
        next: () => {
          this.task.categories.push(newCategory);
          this.newCategoryInput.setValue('');
          this.toastr.success('Категория добавлена');
        },
        error: (err: HttpErrorResponse) => {
          this.toastr.error(
            err.error.message || 'Ошибка при добавлении категории'
          );
        },
      });
  }
  onPriorityChange(newPriority: Priority) {
    this.patch({
      name: this.task.name,
      description: this.task.description,
      dateTo: this.task.dateTo,
      timeLeft: this.task.timeLeft,
      status: this.task.status,
      priority: newPriority,
    });
  }

  onStatusChange(newStatus: TaskStatus) {
    this.patch({
      name: this.task.name,
      description: this.task.description,
      dateTo: this.task.dateTo,
      timeLeft: this.task.timeLeft,
      priority: this.task.priority,
      status: newStatus,
    });
  }

  saveDateTo() {
    if (!this.editDateTo) return;
    const correctDate = new DatePipe('en-US').transform(
      this.editDateTo,
      'dd-MM-yyyy'
    );
    this.form.value.dateTo = correctDate;
    this.patch({
      name: this.task.name,
      description: this.task.description,
      timeLeft: this.task.timeLeft,
      priority: this.task.priority,
      status: this.task.status,
      dateTo: correctDate!,
    });
    this.task.dateTo = this.editDateTo;
    this.showEditDate = false;
  }

  saveTimeLeft() {
    if (!this.editTimeLeft) return;
    this.form.value.timeLeft = this.editTimeLeft;
    this.patch({
      name: this.task.name,
      description: this.task.description,
      priority: this.task.priority,
      status: this.task.status,
      dateTo: this.task.dateTo,

      timeLeft: this.editTimeLeft,
    });
    this.task.timeLeft = this.editTimeLeft;
    this.showEditTime = false;
  }

  cancelEditDate() {
    this.editDateTo = this.task.dateTo || '';
    this.showEditDate = false;
  }

  cancelEditTime() {
    this.editTimeLeft = this.task.timeLeft || '';
    this.showEditTime = false;
  }
}
