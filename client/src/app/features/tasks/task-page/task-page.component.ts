import { Component, inject } from '@angular/core';
import { TasksService } from '../../../core/services/tasks.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, switchMap } from 'rxjs';
import { CommonModule } from '@angular/common';
import { ProjectExecutorDialogComponent } from '../../project/project-executor-dialog/project-executor-dialog.component';
import { ProjectService } from '../../../core/services/project.service';
import { ProfileService } from '../../../core/services/profile.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { ToastrService } from 'ngx-toastr';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TaskPageInfoComponent } from './task-page-info/task-page-info.component';
import { ITasks } from '../../../core/interface/tasks.interface';

@Component({
  selector: 'app-task-page',
  imports: [CommonModule, TaskPageInfoComponent],
  templateUrl: './task-page.component.html',
  styleUrl: './task-page.component.scss',
})
export class TaskPageComponent {
  tasksService = inject(TasksService);
  router = inject(Router);
  route = inject(ActivatedRoute);
  task$: Observable<ITasks | null> = this.route.params.pipe(
    switchMap(({ projectId, taskId }) => {
      return this.tasksService.getTaskById(projectId, taskId);
    })
  );
  projectService = inject(ProjectService);
  profileService = inject(ProfileService);
  users$ = toObservable(this.profileService.users);
  toastr = inject(ToastrService);
  fb = inject(FormBuilder);
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
