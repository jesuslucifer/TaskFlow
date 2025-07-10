import { inject, Injectable, signal } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import { NotificationService } from './notification.service';
import { ToastrService } from 'ngx-toastr';
import { INotificationSocket } from '../interface/notification.interface';

@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private stompClient: Client;
  private readonly socketUrl = 'ws://localhost:8080/ws';
  private readonly token = this.getTokenFromCookie() || '';
  public notifications = signal<INotificationSocket[]>([]);
  notificationService = inject(NotificationService);
  toastr = inject(ToastrService);
  private readonly topics = [
    '/user/queue/deadline-notification',
    '/user/queue/executor-notification',
  ];

  constructor() {
    this.stompClient = new Client({
      brokerURL: this.socketUrl,
      connectHeaders: {
        Authorization: `Bearer ${this.token}`,
      },
      reconnectDelay: 5000,
      debug: () => {},
      onConnect: () => {
        this.topics.forEach((topic) => this.subscribeToTopic(topic));
      },
      onStompError: (frame) => {
        console.log('STOMP ошибка:', frame.headers['message'], frame.body);
      },
    });

    this.stompClient.activate();
  }

  private subscribeToTopic(topic: string) {
    this.stompClient.subscribe(topic, (msg: IMessage) => {
      this.toastr.warning(msg.body);
      let notif: INotificationSocket;
      try {
        notif = JSON.parse(msg.body);
      } catch {
        notif = {
          id: '',
          message: msg.body,
          timestamp: new Date().toISOString(),
        };
      }
      this.notifications.update((prev) => [notif, ...prev]);
    });
  }

  public disconnect() {
    this.stompClient.deactivate();
  }

  private getTokenFromCookie(): string | null {
    const match = document.cookie.match(/(^| )token=([^;]+)/);
    return match ? decodeURIComponent(match[2]) : null;
  }
}
