import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-priority-select',
  imports: [],
  templateUrl: './priority-select.component.html',
  styleUrl: './priority-select.component.scss',
})
export class PrioritySelectComponent {
  @Input() priority!: string;
}
