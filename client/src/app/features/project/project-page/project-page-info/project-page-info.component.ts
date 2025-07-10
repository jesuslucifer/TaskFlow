import {
  Component,
  inject,
  Input,
  OnChanges,
  SimpleChanges,
} from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import {
  ICategory,
  IProject,
  Priority,
  Status,
} from '../../../../core/interface/project.interface';
import { ProjectService } from '../../../../core/services/project.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-project-page-info',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './project-page-info.component.html',
  styleUrl: './project-page-info.component.scss',
})
export class ProjectPageInfoComponent implements OnChanges {
  @Input() project!: IProject;
  @Input() profileId!: number;

  fb = inject(FormBuilder);
  projectService = inject(ProjectService);
  toastr = inject(ToastrService);

  priorityEnum = Priority;
  statusEnum = Status;

  form: FormGroup = this.fb.group({
    name: [''],
    description: [''],
    status: [''],
    priority: [''],
    dateTo: [''],
    timeLeft: [''],
  });

  newCategoryInput = new FormControl<string>('');
  showEditDate = false;
  showEditTime = false;
  showCategoryInput = false;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['project'] && this.project) {
      this.form.patchValue({
        name: this.project.name || '',
        description: this.project.description || '',
        status: this.project.status || '',
        priority: this.project.priority || '',
        dateTo: this.project.dateTo || '',
        timeLeft: this.project.timeLeft || '',
      });

      this.subscribeToFormChanges();
    }
  }

  private subscribeToFormChanges() {
    this.form.get('priority')?.valueChanges.subscribe((value) => {
      this.patch();
    });

    this.form.get('status')?.valueChanges.subscribe((value) => {
      this.patch();
    });
  }

  patch() {
    if (this.form.invalid) return;
    this.projectService
      .patchProject(this.project.id, this.form.value)
      .subscribe({
        next: () => {
          Object.assign(this.project, this.form.value);
          this.toastr.success('Данные обновлены');
        },
        error: (err: HttpErrorResponse) => {
          this.toastr.error(err.error.message || 'Ошибка при обновлении');
        },
      });
  }

  onPriorityChange(priority: Priority) {
    this.form.get('priority')?.setValue(priority);
    this.patch();
  }

  onStatusChange(status: Status) {
    this.form.get('status')?.setValue(status);
    this.patch();
  }

  saveDateTo() {
    const dateTo = this.form.get('dateTo')?.value;
    if (!dateTo) return;

    const formatted = new DatePipe('en-US').transform(dateTo, 'dd-MM-yyyy');
    this.form.get('dateTo')?.setValue(formatted);
    this.patch();
    this.showEditDate = false;
  }

  saveTimeLeft() {
    const timeLeft = this.form.get('timeLeft')?.value;
    if (!timeLeft) return;

    this.patch();
    this.showEditTime = false;
  }

  cancelEditDate() {
    this.form.get('dateTo')?.setValue(this.project.dateTo);
    this.showEditDate = false;
  }

  cancelEditTime() {
    this.form.get('timeLeft')?.setValue(this.project.timeLeft);
    this.showEditTime = false;
  }

  addCategory() {
    const name = this.newCategoryInput.value?.trim();
    if (!name) return;

    const newCategory: ICategory = { name };
    this.projectService.addCategory(this.project.id, newCategory).subscribe({
      next: () => {
        this.project.categories.push(newCategory);
        this.newCategoryInput.setValue('');
        this.toastr.success('Категория добавлена');
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error(
          err.error.message || 'Ошибка при добавлении категории'
        );
      },
    });
  }
}
