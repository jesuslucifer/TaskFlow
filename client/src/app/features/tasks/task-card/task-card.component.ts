import { Component, inject, Input } from '@angular/core';
import { TasksService } from '../../../core/services/tasks.service';
import { MatIcon } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { CdkDrag } from '@angular/cdk/drag-drop';
import { ITasks } from '../../../core/interface/tasks.interface';

@Component({
  selector: 'app-task-card',
  imports: [
    MatIcon,
    RouterLink,
    MatIcon,
    MatButtonModule,
    MatMenuModule,
    CdkDrag,
  ],
  templateUrl: './task-card.component.html',
  styleUrl: './task-card.component.scss',
})
export class TaskCardComponent {
  @Input() task!: ITasks;
  @Input() projectId!: number;
  tasksService = inject(TasksService);
  onDeleteTask() {
    this.tasksService.deleteTask(this.projectId, this.task.id).subscribe();
  }
}
