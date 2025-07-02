import { inject, Injectable, signal } from '@angular/core';
import { INotification } from '../interface/notification.interface';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  http = inject(HttpClient);
  notifications = signal<INotification[]>([]);
  baseApiUrl = 'http://localhost:8080/api';

  getAllNotifications(userId: number) {
    return this.http
      .get<INotification[]>(`${this.baseApiUrl}/notification/${userId}`)
      .pipe(
        tap((res: INotification[]) => {
          this.notifications.set(res);
        })
      );
  }

  removeNotification(notificationId: number) {
    return this.http.delete(
      `${this.baseApiUrl}/notification/${notificationId}`,
      { responseType: 'text' }
    );
  }
}
