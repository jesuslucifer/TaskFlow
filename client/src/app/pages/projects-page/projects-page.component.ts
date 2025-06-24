import { Component, inject } from '@angular/core';
import { ProjectListComponent } from '../../features/project/project-list/project-list.component';
import { MatDialog } from '@angular/material/dialog';
import { ProjectDialogComponent } from '../../features/project/project-dialog/project-dialog.component';
import { ProjectService } from '../../core/services/project.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { ProfileService } from '../../core/services/profile.service';
import { switchMap } from 'rxjs';
@Component({
  selector: 'app-projects-page',
  imports: [ProjectListComponent, CommonModule],
  templateUrl: './projects-page.component.html',
  styleUrl: './projects-page.component.scss',
})
export class ProjectsPageComponent {
  constructor(private dialog: MatDialog) {}
  projectService = inject(ProjectService);
  profileService = inject(ProfileService);
  executorsProjects$ = toObservable(this.projectService.executorsProject);
  projects$ = toObservable(this.projectService.projects);
  ngOnInit(): void {
    this.profileService
      .getMe()
      .pipe(
        switchMap((user) => {
          const userId = user.id;
          return this.projectService.getAllUserProjects(userId);
        })
      )
      .subscribe();

    this.profileService
      .getMe()
      .pipe(
        switchMap((user) => this.projectService.getProjectExecutors(user.id))
      )
      .subscribe();
  }

  openCreateDialog() {
    this.dialog.open(ProjectDialogComponent, {
      width: '500px',
      height: '80%',
      disableClose: false,
    });
  }
}
