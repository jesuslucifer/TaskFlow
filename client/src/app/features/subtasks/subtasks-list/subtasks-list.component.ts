import { Component, inject, Input } from '@angular/core';
import { TasksService } from '../../../core/services/tasks.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { SubtasksCardComponent } from '../subtasks-card/subtasks-card.component';
import { ISubtasks } from '../../../core/interface/tasks.interface';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-subtasks-list',
  imports: [CommonModule, SubtasksCardComponent],
  templateUrl: './subtasks-list.component.html',
  styleUrl: './subtasks-list.component.scss',
})
export class SubtasksListComponent {
  @Input() projectId!: number;
  @Input() taskId!: number;
  tasksService = inject(TasksService);
  subtasks$ = toObservable(this.tasksService.subtasks);
  toastr = inject(ToastrService);
  onToggleSubtaskDone(subtask: ISubtasks) {
    this.tasksService
      .updateSubtasks(this.projectId, this.taskId, subtask.id, {
        ...subtask,
        isDone: subtask.isDone,
      })
      .subscribe({
        next: () => {},
        error: (err: HttpErrorResponse) => {
          this.toastr.error(err.error.message);
        },
      });
  }
  onDeleteSubtask(subtask: ISubtasks) {
    this.tasksService
      .deleteSubtask(this.projectId, this.taskId, subtask.id)
      .subscribe({
        next: () => {
          this.toastr.success('Подзадача успешно удалена');
        },
        error: (err: HttpErrorResponse) => {
          this.toastr.error(err.error.message);
        },
      });
  }
  ngOnInit() {
    this.tasksService.getAllSubtasks(this.projectId, this.taskId).subscribe();
  }
}
