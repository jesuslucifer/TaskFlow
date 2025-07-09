import {
  ChangeDetectionStrategy,
  Component,
  inject,
  Input,
} from '@angular/core';
import { TasksListComponent } from './tasks-list/tasks-list.component';
import { MatDialog } from '@angular/material/dialog';
import { Priority } from '../../../core/interface/project.interface';
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
  ITaskUpdate,
  TaskStatus,
} from '../../../core/interface/tasks.interface';
import { CreateDialogComponent } from '../../../shared/create-dialog/create-dialog.component';

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
  status = TaskStatus;
  todoTasks$ = this.tasks$.pipe(
    map((tasks) => tasks?.filter((t) => t.status === TaskStatus.TODO))
  );

  inProgressTasks$ = this.tasks$.pipe(
    map((tasks) => tasks?.filter((t) => t.status === TaskStatus.PROGRESS))
  );

  doneTasks$ = this.tasks$.pipe(
    map((tasks) => tasks?.filter((t) => t.status === TaskStatus.DONE))
  );
  ngOnInit() {
    this.tasksService.getTasks(this.projectId).subscribe();
  }

  priority = Priority;

  openCreateDialog() {
    this.dialog.open(CreateDialogComponent, {
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

  onTaskDrop(event: CdkDragDrop<ITasks[]>, newTaskStatus: TaskStatus) {
    const prev = event.previousContainer.data;
    const curr = event.container.data;

    if (event.previousContainer === event.container) {
      moveItemInArray(curr, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(prev, curr, event.previousIndex, event.currentIndex);
    }

    const movedTask = curr[event.currentIndex];

    const updatedTask: ITaskUpdate = {
      status: newTaskStatus,
      name: movedTask.name,
      description: movedTask.description,
      priority: movedTask.priority,
      dateTo: movedTask.dateTo,
      timeLeft: movedTask.timeLeft,
    };

    this.tasksService
      .updateTask(updatedTask, this.projectId, movedTask.id)
      .subscribe((newTask) => {
        const currentTasks = this.tasksService.tasks();

        if (currentTasks) {
          const newTasks = currentTasks.map((task) =>
            task.id === newTask.id ? newTask : task
          );

          this.tasksService.tasks.set(newTasks);
        }

        this.toastr.success('Статус задачи обновлён');
      });
  }
}
