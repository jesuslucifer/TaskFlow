import { Component, Input } from '@angular/core';
import { IProject } from '../../../core/interface/project.interface';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-project-card',
  imports: [MatIcon],
  templateUrl: './project-card.component.html',
  styleUrl: './project-card.component.scss',
})
export class ProjectCardComponent {
  @Input() project?: IProject;
  projectProgressPercent(): number {
    return Math.round(
      (Number(this.project?.tasksCompleted) /
        Number(this.project?.tasksCount)) *
        100
    );
  }
}
