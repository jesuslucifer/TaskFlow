import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { MatDialogRef } from '@angular/material/dialog';
import { ProjectDialogComponent } from '../project-dialog.component';
import { ProjectService } from '../../../../core/services/project.service';
import { Priority } from '../../../../core/interface/project.interface';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-project-form-field',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './project-form-field.component.html',
  styleUrl: './project-form-field.component.scss',
})
export class ProjectFormFieldComponent {
  readonly dialogRef = inject(MatDialogRef<ProjectDialogComponent>);
  priorityEnum = Priority;
  projectService = inject(ProjectService);
  router = inject(Router);
  toastr = inject(ToastrService);

  category: string[] = [];

  projectForm: FormGroup = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
    priority: new FormControl<string>(this.priorityEnum.MEDIUM),
    dateTo: new FormControl<string | null>(null),
    timeLeft: new FormControl<string | null>(''),
    categories: new FormControl([]),
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
      this.projectService.createProject(formValue).subscribe({
        next: () => {
          this.toastr.success('Проект успешно создан');
          this.dialogRef.close();
        },
        error: (err: HttpErrorResponse) => {
          this.toastr.error(err.error);
        },
      });
    }
  };
}
