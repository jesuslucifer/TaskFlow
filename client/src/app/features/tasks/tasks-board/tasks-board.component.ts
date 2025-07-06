import {
  ChangeDetectionStrategy,
  Component,
  inject,
  Input,
} from '@angular/core';
import { TasksListComponent } from '../tasks-list/tasks-list.component';
import { MatDialog } from '@angular/material/dialog';
import { Priority, Status } from '../../../core/interface/project.interface';
import { ProjectDialogComponent } from '../../project/project-dialog/project-dialog.component';
import { TasksService } from '../../../core/services/tasks.service';
import { ToastrService } from 'ngx-toastr';
import { toObservable } from '@angular/core/rxjs-interop';
import {
  CdkDragDrop,
  DragDropModule,
  moveItemInArray,
  transferArrayItem,
} from '@angular/cdk/drag-drop';
import { CommonModule } from '@angular/common';
import { map } from 'rxjs';
import {
  ITasks,
  ITasksResponse,
} from '../../../core/interface/tasks.interface';

@Component({
  selector: 'app-tasks-board',
  imports: [TasksListComponent, DragDropModule, CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './tasks-board.component.html',
  styleUrl: './tasks-board.component.scss',
  standalone: true,
})
export class TasksBoardComponent {
  @Input() projectId!: number;
  dialog: MatDialog = inject(MatDialog);
  tasksService = inject(TasksService);
  toastr = inject(ToastrService);
  tasks$ = toObservable(this.tasksService.tasks);
  status = Status;
  todoTasks$ = this.tasks$.pipe(
    map((tasks) => tasks?.filter((t) => t.status === Status.DRAFT))
  );

  inProgressTasks$ = this.tasks$.pipe(
    map((tasks) => tasks?.filter((t) => t.status === Status.ACTIVE))
  );

  doneTasks$ = this.tasks$.pipe(
    map((tasks) => tasks?.filter((t) => t.status === Status.COMPLETED))
  );
  ngOnInit() {
    this.tasksService.getTasks(this.projectId).subscribe();
  }

  priority = Priority;

  openCreateDialog() {
    this.dialog.open(ProjectDialogComponent, {
      width: '500px',
      data: {
        onSave: (formValue: ITasksResponse) => {
          this.tasksService.createTask(formValue, this.projectId).subscribe({
            next: () => {
              this.toastr.success('Задача успешно создана');
              this.dialog.closeAll();
            },
          });
        },
      },
      height: '80%',
      disableClose: false,
    });
  }

  onTaskDrop(event: CdkDragDrop<ITasks[]>, newStatus: Status) {
    const prev = event.previousContainer.data;
    const curr = event.container.data;

    if (event.previousContainer === event.container) {
      moveItemInArray(curr, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(prev, curr, event.previousIndex, event.currentIndex);
    }

    const movedTask = curr[event.currentIndex];
    console.log(newStatus);

    const updatedTask: ITasks = {
      ...movedTask,
      status: newStatus,
    };

    this.tasksService
      .updateTask(updatedTask, this.projectId, updatedTask.id)
      .subscribe(() => {
        const currentTasks = this.tasksService.tasks();

        if (currentTasks) {
          const newTasks = currentTasks.map((task) =>
            task.id === updatedTask.id ? updatedTask : task
          );

          this.tasksService.tasks.set(newTasks);
        }

        this.toastr.success('Статус задачи обновлён');
      });
  }
}
