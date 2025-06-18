import { Component } from '@angular/core';
import { MatDialogActions, MatDialogContent } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ProjectFormFieldComponent } from './project-form-field/project-form-field.component';
@Component({
  selector: 'app-project-dialog',
  imports: [MatDialogContent, MatFormFieldModule, ProjectFormFieldComponent],
  templateUrl: './project-dialog.component.html',
  styleUrl: './project-dialog.component.scss',
})
export class ProjectDialogComponent {}
