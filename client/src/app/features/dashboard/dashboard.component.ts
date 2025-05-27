import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ProjectListComponent } from '../project/project-list/project-list.component';
import { IUser, IUserStats } from '../../core/interface/user.interface';
import { AuthService } from '../../core/services/auth.service';
import { IProfile, ProfileService } from '../../core/services/profile.service';
import { Observable, switchMap } from 'rxjs';
import { toObservable } from '@angular/core/rxjs-interop';
@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterModule, ProjectListComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  authService = inject(AuthService);
  profileService = inject(ProfileService);
  router = inject(Router);
  route = inject(ActivatedRoute);
  avatar = this.profileService.getImage();
  me$ = toObservable(this.profileService.me);
  profile$ = this.route.params.pipe(
    switchMap(({ id }) => {
      if (id === 'me') return this.me$;
      return this.profileService.getProfile(id);
    })
  );
  logout() {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['/login']);
    });
  }

  userStats: IUserStats = {
    activeProjects: 5,
    completedProjects: 12,
    overdueTasks: 3,
    completedTasks: 87,
  };

  activity = {
    tasksThisWeek: 2,
    chartData: [1, 2, 1.5, 2, 1.8, 2.2, 1.9],
  };
}
