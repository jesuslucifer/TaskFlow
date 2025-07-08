import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { ISubtasks } from '../../../core/interface/tasks.interface';

@Component({
  selector: 'app-subtasks-card',
  standalone: true,
  imports: [],
  templateUrl: './subtasks-card.component.html',
  styleUrl: './subtasks-card.component.scss',
})
export class SubtasksCardComponent {
  @Input() subtask!: ISubtasks;
  @Output() toggleDone = new EventEmitter<ISubtasks>();
  @Output() delete = new EventEmitter<void>();
  onDelete() {
    this.delete.emit();
  }
  onToggleDone() {
    this.toggleDone.emit({
      ...this.subtask,
      isDone: !this.subtask.isDone, // меняем локально
    });
  }
}
