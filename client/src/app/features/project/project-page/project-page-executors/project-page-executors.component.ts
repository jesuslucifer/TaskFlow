import { Component, inject, Input } from '@angular/core';
import {
  IExecutors,
  IProject,
} from '../../../../core/interface/project.interface';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProjectService } from '../../../../core/services/project.service';

@Component({
  selector: 'app-project-page-executors',
  imports: [CommonModule, RouterLink],
  templateUrl: './project-page-executors.component.html',
  styleUrl: './project-page-executors.component.scss',
})
export class ProjectPageExecutorsComponent {
  @Input() executors: IExecutors[] = [];
  @Input() projectId!: number;
  @Input() profileId!: number;
  @Input() project!: IProject;

  projectService = inject(ProjectService);
  onDeleteExecutor(projectId: number, executorId: number) {
    this.projectService.deleteExecutor(projectId, executorId).subscribe();
  }
}
