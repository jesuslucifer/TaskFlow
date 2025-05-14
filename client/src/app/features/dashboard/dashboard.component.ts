import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProjectListComponent } from '../project/project-list/project-list.component';
import { IUser, IUserStats } from '../../core/interface/user.interface';
@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterModule, ProjectListComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  user: IUser = {
    name: 'Алексей Иванов',
    email: 'ivanov@example.com',
    avatarUrl: 'assets/avatar.jpg',
  };

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
