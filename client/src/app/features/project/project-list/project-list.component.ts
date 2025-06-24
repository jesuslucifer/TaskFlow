import { Component, effect, inject, Input } from '@angular/core';
import { ProjectCardComponent } from '../project-card/project-card.component';
import { ProjectService } from '../../../core/services/project.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { ProfileService } from '../../../core/services/profile.service';
import { FormsModule } from '@angular/forms';
import { IProject } from '../../../core/interface/project.interface';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-project-list',
  imports: [ProjectCardComponent, CommonModule, FormsModule],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.scss',
})
export class ProjectListComponent {
  @Input() projects$!: Observable<IProject[] | null>;
}
