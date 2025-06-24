import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IExecutors } from '../../../../core/interface/project.interface';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-project-page-executors',
  imports: [CommonModule, RouterLink],
  templateUrl: './project-page-executors.component.html',
  styleUrl: './project-page-executors.component.scss',
})
export class ProjectPageExecutorsComponent {
  @Input() executors: IExecutors[] = [];
}
