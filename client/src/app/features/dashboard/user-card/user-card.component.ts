import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { toObservable } from '@angular/core/rxjs-interop';
import { AuthService } from '../../../core/services/auth.service';
import {
  IProfile,
  ProfileService,
} from '../../../core/services/profile.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-card.component.html',
  styleUrl: './user-card.component.scss',
})
export class UserCardComponent {
  authService = inject(AuthService);
  profileService = inject(ProfileService);
  router = inject(Router);
  toastr = inject(ToastrService);

  route = inject(ActivatedRoute);

  me$ = toObservable(this.profileService.me);
  profile$: Observable<IProfile | null> = this.route.params.pipe(
    switchMap(({ id }) =>
      id === 'me' ? this.me$ : this.profileService.getProfile(id)
    ),
    catchError((err: HttpErrorResponse) => {
      this.toastr.error(err.error.message);
      this.router.navigate(['/profile/me']);
      return throwError(() => err);
    })
  );

  avatar: File | null = null;
  preview = signal<string | null>(null);
  userStats = {
    activeProjects: 5,
    completedProjects: 12,
    overdueTasks: 3,
    completedTasks: 87,
  };

  uploadImage(file: File) {
    this.profileService.uploadImage(file).subscribe({
      next: (fileUrl) => {
        this.profileService.getMe().subscribe();
        this.preview.set(fileUrl);
        this.toastr.success('Аватарка успешно загружена!');
      },
      error: (err) => this.toastr.error(err.error.message),
    });
  }

  fileBrowserHandler(event: Event) {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (!file || !file.type.match('image')) return;

    const reader = new FileReader();
    reader.onload = (event) => {
      this.preview.set(event.target?.result?.toString() || null);
    };
    reader.readAsDataURL(file);

    this.avatar = file;
    this.uploadImage(this.avatar);
  }
}
