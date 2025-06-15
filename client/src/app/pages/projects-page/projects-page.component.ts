import { Component } from '@angular/core';
import { ProjectListComponent } from '../../features/project/project-list/project-list.component';

@Component({
  selector: 'app-projects-page',
  imports: [ProjectListComponent],
  templateUrl: './projects-page.component.html',
  styleUrl: './projects-page.component.scss',
})
export class ProjectsPageComponent {}
