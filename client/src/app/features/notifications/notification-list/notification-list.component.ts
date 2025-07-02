import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../../core/services/notification.service';
import { ProfileService } from '../../../core/services/profile.service';
import { toObservable } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-notification',
  imports: [CommonModule, FormsModule],
  templateUrl: './notification-list.component.html',
  styleUrl: './notification-list.component.scss',
})
export class NotificationComponent {
  private notificationService = inject(NotificationService);
  profileService = inject(ProfileService);
  me$ = toObservable(this.profileService.me);
  notifications$ = toObservable(this.notificationService.notifications);
  ngOnInit() {
    this.me$.subscribe((me) => {
      this.notificationService.getAllNotifications(me!.id).subscribe();
    });
  }
  onRemoveNotification(notificationId: number) {
    this.notificationService.removeNotification(notificationId).subscribe();
  }
}
