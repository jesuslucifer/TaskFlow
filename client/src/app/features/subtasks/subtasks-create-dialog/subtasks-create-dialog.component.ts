import { Component, Inject, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Priority } from '../../../core/interface/project.interface';
import { TasksService } from '../../../core/services/tasks.service';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-subtasks-create-dialog',
  imports: [ReactiveFormsModule],
  templateUrl: './subtasks-create-dialog.component.html',
  styleUrl: './subtasks-create-dialog.component.scss',
})
export class SubtasksCreateDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { projectId: number; taskId: number }
  ) {}
  dialog: MatDialog = inject(MatDialog);
  toastr = inject(ToastrService);
  priorityEnum = Priority;
  tasksService = inject(TasksService);
  form: FormGroup = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
    status: new FormControl(''),
    priority: new FormControl(''),
  });
  onSubmit() {
    if (this.form.valid) {
      console.log(this.form.value);
      this.form.value.status = 'ACTIVE';
      this.form.value.priority = 'LOW';

      this.tasksService
        .createSubtask(this.data.projectId, this.data.taskId, this.form.value)
        .subscribe({
          next: () => {
            this.toastr.success('Подзадача успешно добавлена');
            this.dialog.closeAll();
          },
          error: (err: HttpErrorResponse) => {
            this.toastr.error(err.error.message);
          },
        });
    }
  }
}
