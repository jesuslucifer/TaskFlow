import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-activity',
  imports: [CommonModule],
  templateUrl: './activity.component.html',
  styleUrl: './activity.component.scss',
})
export class ActivityComponent {
  activity = {
    tasksThisWeek: 2,
    chartData: [1, 2, 1.5, 2, 1.8, 2.2, 1.9],
  };
}
