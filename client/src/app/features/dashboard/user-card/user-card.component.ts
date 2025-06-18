import { Component, inject, Input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProfileService } from '../../../core/services/profile.service';
import { ToastrService } from 'ngx-toastr';
import { IProfile } from '../../../core/interface/user.interface';

@Component({
  selector: 'app-user-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-card.component.html',
  styleUrl: './user-card.component.scss',
})
export class UserCardComponent {
  profileService = inject(ProfileService);
  toastr = inject(ToastrService);
  @Input() profile!: IProfile;
  @Input() me!: IProfile | null;

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
