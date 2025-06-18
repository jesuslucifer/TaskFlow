import { Component } from '@angular/core';
import { ProjectListComponent } from '../../features/project/project-list/project-list.component';
import { MatDialog } from '@angular/material/dialog';
import { ProjectDialogComponent } from '../../features/project/project-dialog/project-dialog.component';
@Component({
  selector: 'app-projects-page',
  imports: [ProjectListComponent],
  templateUrl: './projects-page.component.html',
  styleUrl: './projects-page.component.scss',
})
export class ProjectsPageComponent {
  constructor(private dialog: MatDialog) {}

  openCreateDialog() {
    this.dialog.open(ProjectDialogComponent, {
      width: '500px',
      disableClose: false,
    });
  }
}
