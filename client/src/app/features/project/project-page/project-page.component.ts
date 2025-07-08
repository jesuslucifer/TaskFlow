import { Component, inject } from '@angular/core';
import { ProjectService } from '../../../core/services/project.service';
import { ProfileService } from '../../../core/services/profile.service';
import { Observable, switchMap, tap } from 'rxjs';
import { IProject } from '../../../core/interface/project.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule, Location } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ProjectExecutorDialogComponent } from '../project-executor-dialog/project-executor-dialog.component';
import { toObservable } from '@angular/core/rxjs-interop';
import { ProjectPageExecutorsComponent } from './project-page-executors/project-page-executors.component';
import { ProjectPageInfoComponent } from './project-page-info/project-page-info.component';
import { ToastrService } from 'ngx-toastr';
import { TasksBoardComponent } from '../../tasks/tasks-board/tasks-board.component';
@Component({
  selector: 'app-project-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ProjectPageExecutorsComponent,
    ProjectPageInfoComponent,
    TasksBoardComponent,
  ],
  templateUrl: './project-page.component.html',
  styleUrl: './project-page.component.scss',
})
export class ProjectPageComponent {
  projectService = inject(ProjectService);
  profileService = inject(ProfileService);
  router = inject(Router);
  users$ = toObservable(this.profileService.users);
  toastr = inject(ToastrService);
  route = inject(ActivatedRoute);
  fb = inject(FormBuilder);
  location = inject(Location);

  projectId: number = -1;
  profileId: number = -1;
  me = this.profileService.me;
  form: FormGroup = this.fb.group({
    name: [{ value: '' }],
    description: [{ value: '' }],
    status: [{ value: '' }],
    priority: [{ value: '' }],
    dateTo: [{ value: '' }],
    timeLeft: [{ value: '' }],
  });

  dialog: MatDialog = inject(MatDialog);
  project$: Observable<IProject | null> = this.route.params.pipe(
    switchMap(({ username, name }) => {
      return this.projectService.getProjectByName(username, name).pipe(
        tap((project) => {
          this.form.patchValue(project);
          this.projectId = project.id;
          this.profileId = this.me()!.id;
        })
      );
    })
  );
  isEditingNameDesc = false;

  toggleEditNameDesc() {
    this.isEditingNameDesc = !this.isEditingNameDesc;
  }

  saveNameDesc() {
    const { name, description } = this.form.value;
    this.projectService
      .patchProject(this.projectId, { ...this.form.value, name, description })
      .subscribe({
        next: () => {
          this.isEditingNameDesc = false;
          this.toastr.success('Данные изменены');
        },
      });
  }
  goBack(): void {
    this.location.back();
  }
  openCreateDialog() {
    this.dialog.open(ProjectExecutorDialogComponent, {
      width: '500px',
      data: {
        projectId: this.projectId,
      },
      height: '80%',
      disableClose: false,
    });
  }
}
