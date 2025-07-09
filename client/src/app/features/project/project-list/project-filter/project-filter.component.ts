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
import { ProjectFilter } from '../../../../core/interface/project.interface';
@Component({
  selector: 'app-project-filter',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, MatIcon],
  templateUrl: './project-filter.component.html',
  styleUrls: ['./project-filter.component.scss'],
})
export class ProjectFilterComponent {
  @Output() filterChange = new EventEmitter<ProjectFilter>();

  sortedProjects = new FormGroup({
    name: new FormControl<string | null>(null),
    sortBy: new FormControl<'name' | 'dateTo' | 'priority' | 'status'>('name'),
    sortOrder: new FormControl<'asc' | 'desc'>('asc'),
    status: new FormControl<string>(''),
    priority: new FormControl<string>(''),
  });

  constructor() {
    this.sortedProjects.valueChanges
      .pipe(debounceTime(300))
      .subscribe((value) => {
        this.filterChange.emit(value as ProjectFilter);
      });
  }
}
