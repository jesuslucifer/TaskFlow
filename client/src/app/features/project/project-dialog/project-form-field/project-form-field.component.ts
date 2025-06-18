import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { MatDialogRef } from '@angular/material/dialog';
import { ProjectDialogComponent } from '../project-dialog.component';
import { ProjectService } from '../../../../core/services/project.service';
enum Priority {
  LOW = 'LOW',
  HIGH = 'HIGH',
  MEDIUM = 'MEDIUM',
  CRITICAL = 'CRITICAL',
}
@Component({
  selector: 'app-project-form-field',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './project-form-field.component.html',
  styleUrl: './project-form-field.component.scss',
})
export class ProjectFormFieldComponent {
  readonly dialogRef = inject(MatDialogRef<ProjectDialogComponent>);
  priorityEnum = Priority;
  projectService = inject(ProjectService);
  router = inject(Router);
  toastr = inject(ToastrService);
  projectForm: FormGroup = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
    priority: new FormControl<string>(this.priorityEnum.MEDIUM),
    dateTo: new FormControl<string | null>(null),
    timeLeft: new FormControl<string | null>(''),
  });

  onSubmit = () => {
    if (this.projectForm.valid) {
      const formValue = this.projectForm.value;

      if (formValue.dateTo) {
        formValue.dateTo = new DatePipe('en-US').transform(
          formValue.dateTo,
          'dd-MM-yyyy'
        );
      }
      this.projectService.createProject(this.projectForm.value).subscribe({
        next: () => {
          this.toastr.success('Проект успешно создан');
          this.dialogRef.close();
          // console.log(this.projectForm.value);
        },
        error: () => {
          this.toastr.error('Ошибка при создании проекта');
        },
      });
    }
  };
}
