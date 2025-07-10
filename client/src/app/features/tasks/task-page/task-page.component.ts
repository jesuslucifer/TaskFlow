import { Component, inject } from '@angular/core';
import { TasksService } from '../../../core/services/tasks.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, switchMap } from 'rxjs';
import { CommonModule, Location } from '@angular/common';
import { ProjectService } from '../../../core/services/project.service';
import { ProfileService } from '../../../core/services/profile.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { ToastrService } from 'ngx-toastr';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TaskPageInfoComponent } from './task-page-info/task-page-info.component';
import { ITasks } from '../../../core/interface/tasks.interface';
import { SubtasksCreateDialogComponent } from '../../subtasks/subtasks-create-dialog/subtasks-create-dialog.component';
import { SubtasksListComponent } from '../../subtasks/subtasks-list/subtasks-list.component';

@Component({
  selector: 'app-task-page',
  imports: [
    CommonModule,
    TaskPageInfoComponent,
    SubtasksListComponent,
    ReactiveFormsModule,
    FormsModule,
  ],
  templateUrl: './task-page.component.html',
  styleUrl: './task-page.component.scss',
})
export class TaskPageComponent {
  tasksService = inject(TasksService);
  router = inject(Router);
  route = inject(ActivatedRoute);
  taskId: number = -1;
  projectId: number = -1;
  location = inject(Location);
  task$: Observable<ITasks | null> = this.route.params.pipe(
    switchMap(({ projectId, taskId }) => {
      this.projectId = projectId;
      this.taskId = taskId;
      return this.tasksService.getTaskById(projectId, taskId);
    })
  );
  projectService = inject(ProjectService);
  profileService = inject(ProfileService);
  users$ = toObservable(this.profileService.users);
  toastr = inject(ToastrService);
  fb = inject(FormBuilder);
  me = this.profileService.me;
  form: FormGroup = this.fb.group({
    name: '',
    description: '',
  });

  dialog: MatDialog = inject(MatDialog);

  isEditingNameDesc = false;
  goBack(): void {
    this.location.back();
  }
  toggleEditNameDesc() {
    this.isEditingNameDesc = !this.isEditingNameDesc;
  }

  saveNameDesc(task: ITasks) {
    const { name, description } = this.form.value;
    this.tasksService
      .updateTask(
        {
          dateTo: task.dateTo,
          timeLeft: task.timeLeft,
          status: task.status,
          priority: task.priority,
          name,
          description,
        },
        this.projectId,
        this.taskId
      )
      .subscribe({
        next: () => {
          this.isEditingNameDesc = false;
          this.toastr.success('Данные изменены');
        },
      });
  }

  openCreateDialog() {
    this.dialog.open(SubtasksCreateDialogComponent, {
      width: '500px',
      data: {
        projectId: this.projectId,
        taskId: this.taskId,
      },
      height: '35%',
      disableClose: false,
    });
  }
}
