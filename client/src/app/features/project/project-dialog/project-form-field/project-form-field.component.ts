import { CommonModule, DatePipe } from '@angular/common';
import { Component, Inject, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProjectDialogComponent } from '../project-dialog.component';
import { ProjectService } from '../../../../core/services/project.service';
import {
  IProject,
  Priority,
} from '../../../../core/interface/project.interface';
import { HttpErrorResponse } from '@angular/common/http';
import { TasksService } from '../../../../core/services/tasks.service';
import { minTodayValidator } from '../../../../shared/validators/min.days.validators';

@Component({
  selector: 'app-project-form-field',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './project-form-field.component.html',
  styleUrl: './project-form-field.component.scss',
})
export class ProjectFormFieldComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: { onSave: any }) {}
  readonly dialogRef = inject(MatDialogRef<ProjectDialogComponent>);
  priorityEnum = Priority;
  projectService = inject(ProjectService);
  tasksService = inject(TasksService);

  router = inject(Router);
  toastr = inject(ToastrService);

  category: string[] = [];

  projectForm: FormGroup = new FormGroup({
    name: new FormControl<string | null>(null, [Validators.required]),
    description: new FormControl<string | null>(''),
    priority: new FormControl<string>(this.priorityEnum.MEDIUM),
    // dateTo: new FormControl<string>('', [Validators.required]),
    dateTo: new FormControl<string>('', [
      Validators.required,
      minTodayValidator(),
    ]),

    timeLeft: new FormControl<string>('', [Validators.required]),
    categories: new FormControl<string[]>([]),
  });

  categoryInput = new FormControl<string | null>('');

  onEnterCategory(event: any) {
    event.preventDefault();

    const inputValue = this.categoryInput.value?.trim();
    if (inputValue && !this.category.includes(inputValue)) {
      this.category.push(inputValue);
      this.categoryInput.setValue('');
      this.projectForm.patchValue({ categories: this.category });
    }
  }

  removeCategory(name: string) {
    this.category = this.category.filter((c) => c !== name);
    this.projectForm.patchValue({ categories: this.category });
  }

  onSubmit = () => {
    if (this.projectForm.valid) {
      const formValue = { ...this.projectForm.value };

      formValue.categories = this.category.map((name) => ({ name }));
      if (formValue.dateTo) {
        formValue.dateTo = new DatePipe('en-US').transform(
          formValue.dateTo,
          'dd-MM-yyyy'
        );
      }
      this.data.onSave(formValue);
    }
  };
}
