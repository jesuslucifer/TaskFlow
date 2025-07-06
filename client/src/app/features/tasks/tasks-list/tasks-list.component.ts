import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CdkDragDrop, DragDropModule } from '@angular/cdk/drag-drop';
import { Status } from '../../../core/interface/project.interface';
import { TaskCardComponent } from '../task-card/task-card.component';
import { ITasks } from '../../../core/interface/tasks.interface';

@Component({
  selector: 'app-tasks-list',
  standalone: true,
  imports: [CommonModule, TaskCardComponent, DragDropModule],
  templateUrl: './tasks-list.component.html',
  styleUrl: './tasks-list.component.scss',
})
export class TasksListComponent {
  @Input() tasks: ITasks[] = [];
  @Input() status!: Status;
  @Input() projectId!: number;
}
