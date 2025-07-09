import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, Output, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { debounceTime, startWith, switchMap } from 'rxjs';
import { ProjectService } from '../../../../core/services/project.service';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-project-filter',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, MatIcon],
  templateUrl: './project-filter.component.html',
  styleUrls: ['./project-filter.component.scss'],
})
export class ProjectFilterComponent {
  profileService = inject(ProjectService);
  projectService = inject(ProjectService);

  constructor() {
    this.sortedProjects.valueChanges
      .pipe(
        startWith({ name: '' }),
        debounceTime(300),
        switchMap((formValue) => {
          return this.profileService.findProjectsByName(formValue.name);
        })
      )
      .subscribe();
  }
  sortedProjects: FormGroup = new FormGroup({
    name: new FormControl<string | null>(null),
    dateTo: new FormControl<string>(''),
    timeLeft: new FormControl<string>(''),
  });
  onFilteredProjectsByName(event: any) {
    const sortOrder = event.target.value;
    this.projectService.filtredProjectsByName(sortOrder).subscribe();
  }
  onFilteredProjectsByDate(event: any) {
    const sortOrder = event.target.value;
    this.projectService.filtredProjectsByDate(sortOrder).subscribe();
  }
}
