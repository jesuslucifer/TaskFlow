import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, inject, INJECTOR } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ProjectService } from '../../../core/services/project.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-project-form-field',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './project-form-field.component.html',
  styleUrl: './project-form-field.component.scss',
})
export class ProjectFormFieldComponent {
  http = inject(HttpClient);
  projectService = inject(ProjectService);
  toastr = inject(ToastrService);
  projectForm: FormGroup = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
    // projectStatus: new FormControl(''),
    // projectPriority: new FormControl(''),
    // projectCategory: new FormControl(''),
    // projectManager: new FormControl(''),
    // teamMembers: new FormControl(''),
    // projectType: new FormControl(''),
  });

  onSubmit = () => {
    if (this.projectForm.valid) {
      this.projectService.createProject(this.projectForm.value, 43).subscribe({
        next: (response) => {
          this.toastr.success('Проект успешно создан');
          console.log(response);
        },
        error: () => {
          this.toastr.error('Ошибка при создании проекта');
        },
      });
      console.log(this.projectForm.value);
    }
  };
}
