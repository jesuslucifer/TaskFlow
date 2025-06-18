import { Component, inject } from '@angular/core';
import { ProjectService } from '../../../core/services/project.service';
import { ProfileService } from '../../../core/services/profile.service';
import { Observable, switchMap, tap } from 'rxjs';
import { IProject } from '../../../core/interface/project.interface';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-project-page',
  imports: [CommonModule],
  templateUrl: './project-page.component.html',
  styleUrl: './project-page.component.scss',
})
export class ProjectPageComponent {
  projectService = inject(ProjectService);
  profileService = inject(ProfileService);
  route = inject(ActivatedRoute);

  project$: Observable<IProject | null> = this.route.params.pipe(
    switchMap(({ username, name }) => {
      return this.projectService.getProjectByName(username, name).pipe(
        tap((project) => {
          console.log(project);
        })
      );
    })
  );
  tasks = [
    {
      status: 'In Progress',
      title: 'Manage Finances',
      assigned: 'mustafa',
      priority: 'High',
    },
    {
      status: 'In Progress',
      title: 'Re design the logo',
      assigned: 'mustafa',
      priority: 'Small',
    },
    {
      status: 'Done',
      title: 'Start how to use the service course',
      assigned: 'mustafa',
      priority: 'Very High',
    },
  ];
}
