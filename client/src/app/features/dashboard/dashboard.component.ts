import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProjectListComponent } from '../project/project-list/project-list.component';
import { UserCardComponent } from './user-card/user-card.component';
import { ActivityComponent } from './activity/activity.component';
import { ProfileService } from '../../core/services/profile.service';
import { toObservable } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    UserCardComponent,
    ActivityComponent,
    ProjectListComponent,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  tasks = [
    'Созвон с заказчиком',
    'Финализировать макет',
    'Обновить документацию',
  ];
}
