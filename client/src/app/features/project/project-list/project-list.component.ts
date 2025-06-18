import { Component, effect, inject } from '@angular/core';
import { ProjectCardComponent } from '../project-card/project-card.component';
import { ProjectService } from '../../../core/services/project.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { ProfileService } from '../../../core/services/profile.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-project-list',
  imports: [ProjectCardComponent, CommonModule, FormsModule],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.scss',
})
export class ProjectListComponent {
  projectsService = inject(ProjectService);
  profileService = inject(ProfileService);
  projects$ = toObservable(this.projectsService.projects);

  // ngOnInit() {
  //   this.projectsService.getAllProjects().subscribe();
  // }
  // projects: IProject[] = [
  //   {
  //     name: 'Project 1',
  //     description: 'This is project 1.',
  //     dateTo: '2020-01-01',
  //     timeLeft: '1 day',
  //     projectStatus: 'in progress',
  //     projectPriority: 'high',
  //     projectCategory: 'development',
  //   },
  // ];
}
