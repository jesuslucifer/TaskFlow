import { toObservable } from '@angular/core/rxjs-interop';
import { Component, inject, Input } from '@angular/core';
import { IProject } from '../../../core/interface/project.interface';
import { MatIcon } from '@angular/material/icon';
import { ProjectService } from '../../../core/services/project.service';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { ProfileService } from '../../../core/services/profile.service';
import { switchMap } from 'rxjs';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-project-card',
  imports: [MatIcon, MatButtonModule, MatMenuModule, RouterLink],
  templateUrl: './project-card.component.html',
  styleUrl: './project-card.component.scss',
})
export class ProjectCardComponent {
  @Input() project?: IProject;
  projectService = inject(ProjectService);

  onDeleteProject() {
    this.projectService.deleteProject(this.project!.id).subscribe(() => {});
    console.log(this.project);
  }
}
