import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Observable, switchMap } from 'rxjs';
import { toObservable } from '@angular/core/rxjs-interop';
import { AuthService } from '../../../core/services/auth.service';
import {
  IProfile,
  ProfileService,
} from '../../../core/services/profile.service';

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
  route = inject(ActivatedRoute);

  me$ = toObservable(this.profileService.me);
  profile$: Observable<IProfile | null> = this.route.params.pipe(
    switchMap(({ id }) =>
      id === 'me' ? this.me$ : this.profileService.getProfile(id)
    )
  );

  avatar: File | null = null;
  preview = signal<string | null>(null);
  //'assets/default.jpg'
  userStats = {
    activeProjects: 5,
    completedProjects: 12,
    overdueTasks: 3,
    completedTasks: 87,
  };
  consoleAva(avatarUrl: string | null) {
    console.log(`Preview: ${this.preview}`);
    console.log(`Avatar url:${avatarUrl}`);
  }

  uploadImage(file: File) {
    this.profileService.uploadImage(file).subscribe({
      next: (fileUrl) => {
        this.profileService.getMe().subscribe();
        this.preview.set(fileUrl);
      },
      error: (err) => console.error('Ошибка при загрузке аватарки', err),
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
