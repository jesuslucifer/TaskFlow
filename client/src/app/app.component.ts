import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NotificationPopupComponent } from './features/notifications/notification-popup/notification-popup.component';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NotificationPopupComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {}
