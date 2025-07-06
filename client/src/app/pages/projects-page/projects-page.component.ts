import { Component, inject } from '@angular/core';
import { ProjectListComponent } from '../../features/project/project-list/project-list.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ProjectDialogComponent } from '../../features/project/project-dialog/project-dialog.component';
import { ProjectService } from '../../core/services/project.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { ProfileService } from '../../core/services/profile.service';
import { IProject, Priority } from '../../core/interface/project.interface';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
@Component({
  selector: 'app-projects-page',
  imports: [ProjectListComponent, CommonModule],
  templateUrl: './projects-page.component.html',
  styleUrl: './projects-page.component.scss',
})
export class ProjectsPageComponent {
  constructor(private dialog: MatDialog) {}
  projectService = inject(ProjectService);
  profileService = inject(ProfileService);
  executorsProjects$ = toObservable(this.projectService.executorsProject);
  projects$ = toObservable(this.projectService.projects);
  toastr = inject(ToastrService);
  priorityEnum = Priority;

  // dialogRef = inject(MatDialogRef<ProjectDialogComponent>);
  projectForm: FormGroup = new FormGroup({
    name: new FormControl<string | null>(null, [Validators.required]),
    description: new FormControl<string | null>(''),
    priority: new FormControl<string>(this.priorityEnum.MEDIUM),
    dateTo: new FormControl<string>('', [Validators.required]),
    timeLeft: new FormControl<string>('', [Validators.required]),
    categories: new FormControl<string[]>([]),
  });

  openCreateDialog() {
    this.dialog.open(ProjectDialogComponent, {
      width: '500px',
      data: {
        form: this.projectForm,
        onSave: (formValue: IProject) => {
          this.projectService.createProject(formValue).subscribe({
            next: () => {
              this.toastr.success('Проект успешно создан');
              this.dialog.closeAll();
            },
            error: (err: HttpErrorResponse) => {
              this.toastr.error(err.error.message);
            },
          });
        },
      },
      height: '80%',
      disableClose: false,
    });
  }
}
