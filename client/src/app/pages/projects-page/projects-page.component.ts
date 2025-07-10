import { Component, inject } from '@angular/core';
import { ProjectListComponent } from '../../features/project/project-list/project-list.component';
import { MatDialog } from '@angular/material/dialog';
import { ProjectService } from '../../core/services/project.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { ProfileService } from '../../core/services/profile.service';
import {
  IProject,
  Priority,
  ProjectFilter,
} from '../../core/interface/project.interface';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { ProjectFilterComponent } from '../../features/project/project-list/project-filter/project-filter.component';
import { CreateDialogComponent } from '../../shared/create-dialog/create-dialog.component';
@Component({
  selector: 'app-projects-page',
  imports: [ProjectListComponent, CommonModule, ProjectFilterComponent],
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
  me = this.profileService.me;
  onFilterChange(filter: ProjectFilter) {
    this.projectService.filterProjects(filter).subscribe();
    this.projectService.filterProjects(filter, true).subscribe();
  }
  projectForm: FormGroup = new FormGroup({
    name: new FormControl<string | null>(null, [Validators.required]),
    description: new FormControl<string | null>(''),
    priority: new FormControl<string>(this.priorityEnum.MEDIUM),
    dateTo: new FormControl<string>('', [Validators.required]),
    timeLeft: new FormControl<string>('', [Validators.required]),
    categories: new FormControl<string[]>([]),
  });
  ngOnInit() {
    this.projectService.getAllUserProjects().subscribe();
    this.projectService.getProjectExecutors().subscribe();
  }
  openCreateDialog() {
    this.dialog.open(CreateDialogComponent, {
      width: '500px',
      data: {
        form: this.projectForm,
        onSave: (formValue: IProject) => {
          this.projectService.createProject(formValue).subscribe({
            next: () => {
              this.projectService.getAllUserProjects().subscribe();
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
