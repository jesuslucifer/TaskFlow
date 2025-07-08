import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IProject } from '../../../core/interface/project.interface';
import { Observable } from 'rxjs';
import { ProjectCardComponent } from './project-card/project-card.component';

@Component({
  selector: 'app-project-list',
  imports: [ProjectCardComponent, CommonModule, FormsModule],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.scss',
})
export class ProjectListComponent {
  @Input() projects$!: Observable<IProject[] | null>;
}
