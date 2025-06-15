import { Component, inject } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { ProfileService } from '../../core/services/profile.service';
import { CommonModule } from '@angular/common';
import { toObservable } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-header',
  imports: [MatIcon, CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  profileService = inject(ProfileService);

  me$ = toObservable(this.profileService.me);
}
