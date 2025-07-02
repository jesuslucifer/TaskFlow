import { CommonModule } from '@angular/common';
import { Component, computed, effect, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WebSocketService } from '../../../core/services/web-socket.service';
import { toObservable } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-notification-popup',
  imports: [CommonModule, FormsModule],
  templateUrl: './notification-popup.component.html',
  styleUrl: './notification-popup.component.scss',
})
export class NotificationPopupComponent {
  private webSocketService = inject(WebSocketService);
  notifications$ = toObservable(
    computed(() => this.webSocketService.notifications())
  );
  ngOnInit() {
    this.notifications$.subscribe();
  }
}
