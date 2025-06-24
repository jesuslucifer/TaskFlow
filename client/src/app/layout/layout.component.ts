import { Component, inject } from '@angular/core';
import { SidebarComponent } from './sidebar/sidebar.component';
import { HeaderComponent } from './header/header.component';
import { RouterOutlet } from '@angular/router';
import { ProfileService } from '../core/services/profile.service';
import { ProjectService } from '../core/services/project.service';
import { toObservable } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet, SidebarComponent, HeaderComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
})
export class LayoutComponent {
  profileService = inject(ProfileService);
  projectsService = inject(ProjectService);
  me$ = toObservable(this.profileService.me);

  ngOnInit() {
    this.profileService.getMe().subscribe((res) => {
      // this.projectsService.getAllUserProjects(res.id).subscribe();
      // this.projectsService.getProjectExecutors(res.id).subscribe();
      this.profileService.getUsers().subscribe();
    });
  }
}
