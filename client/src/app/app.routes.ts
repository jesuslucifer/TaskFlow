import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { RegisterPageComponent } from './pages/authPages/register-page/register-page.component';
import { LoginPageComponent } from './pages/authPages/login-page/login-page.component';
import { ProjectsPageComponent } from './pages/projects-page/projects-page.component';
import { canActivateAuth } from './core/guards/auth.guard';
import { SettingsPageComponent } from './pages/settings-page/settings-page.component';
import { ProjectPageComponent } from './features/project/project-page/project-page.component';
import { TaskPageComponent } from './features/tasks/task-page/task-page.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        redirectTo: 'profile/me',
        pathMatch: 'full',
      },
      {
        path: 'projects/:projectId/tasks/:taskId',
        component: TaskPageComponent,
      },

      {
        path: 'project/:username/:name',
        component: ProjectPageComponent,
      },

      { path: 'projects', component: ProjectsPageComponent },
      { path: 'profile/:id', component: DashboardComponent },

      { path: 'settings', component: SettingsPageComponent },
    ],
    canActivate: [canActivateAuth],
  },
  {
    path: 'sign-up',
    component: RegisterPageComponent,
  },
  {
    path: 'login',
    component: LoginPageComponent,
  },
];
