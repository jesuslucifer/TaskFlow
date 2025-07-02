import { Component, computed, Inject, inject, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { ProfileService } from '../../../core/services/profile.service';
import { ProjectService } from '../../../core/services/project.service';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
enum ExecutorRole {
  DEVELOPER = 'DEVELOPER',
  TESTER = 'TESTER',
  ANALYZER = 'ANALYZER',
  ADMINISTRATOR = 'ADMINISTRATOR',
}
@Component({
  selector: 'app-project-executor-dialog',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './project-executor-dialog.component.html',
  styleUrl: './project-executor-dialog.component.scss',
})
export class ProjectExecutorDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: { projectId: number }
  ) {}
  ngOnInit() {
    this.profileService.getUsers().subscribe();
  }
  profileService = inject(ProfileService);
  projectService = inject(ProjectService);
  readonly dialogRef = inject(MatDialogRef<ProjectExecutorDialogComponent>);
  toastr = inject(ToastrService);
  users$ = this.profileService.users;
  searchQuery = signal<string>('');
  selectedUserId = signal<number | null>(null);
  selectedRoles = signal<Record<number, ExecutorRole>>({});

  executorRoles: ExecutorRole[] = [
    ExecutorRole.DEVELOPER,
    ExecutorRole.TESTER,
    ExecutorRole.ANALYZER,
  ];

  filteredUsers = computed(() => {
    const query = this.searchQuery().toLowerCase().trim();
    const users = this.users$();
    if (!users) return [];
    return users.filter((user) => user.username?.toLowerCase().includes(query));
  });

  toggleSelect(userId: number) {
    const current = this.selectedUserId();
    this.selectedUserId.set(current === userId ? null : userId);
  }

  setRoleForUser(userId: number, role: ExecutorRole) {
    const roles = { ...this.selectedRoles() };
    roles[userId] = role;
    this.selectedRoles.set(roles);
  }

  onAddExecutor(userId: number) {
    const role = this.selectedRoles()[userId] || 'DEVELOPER';
    this.projectService
      .addExecutor(this.data.projectId, {
        executorId: userId,
        executorRole: role,
      })
      .subscribe({
        next: () => {
          this.dialogRef.close();
          this.toastr.success('Исполнитель добавлен');
        },
        error: (err: HttpErrorResponse) => {
          this.toastr.error(err.error.message);
        },
      });
  }
}
