import { Component, inject } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { ProfileService } from '../../core/services/profile.service';
import { CommonModule } from '@angular/common';
import { toObservable } from '@angular/core/rxjs-interop';
import { NotificationComponent } from '../../features/notifications/notification-list/notification-list.component';
import { ClickOutsideDirective } from '../../shared/directives/click-outside.directive';

@Component({
  selector: 'app-header',
  imports: [
    MatIcon,
    CommonModule,
    NotificationComponent,
    ClickOutsideDirective,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  profileService = inject(ProfileService);
  showNotif: boolean = false;
  me$ = toObservable(this.profileService.me);
  showNotification(show: boolean) {
    this.showNotif = show;
  }
  hideNotification() {
    this.showNotif = false;
  }
}
