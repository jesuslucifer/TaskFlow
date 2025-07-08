import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProjectListComponent } from '../project/project-list/project-list.component';
import { UserCardComponent } from './user-card/user-card.component';
import { ActivityComponent } from './activity/activity.component';
import { toObservable } from '@angular/core/rxjs-interop';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../core/services/auth.service';
import { ProfileService } from '../../core/services/profile.service';
import { IProfile } from '../../core/interface/user.interface';
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    UserCardComponent,
    ActivityComponent,
    FormsModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
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

  tasks = [
    'Созвон с заказчиком',
    'Финализировать макет',
    'Обновить документацию',
  ];
}
