import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { RegisterPageComponent } from './pages/authPages/register-page/register-page.component';
import { LoginPageComponent } from './pages/authPages/login-page/login-page.component';
import { ProjectsPageComponent } from './pages/projects-page/projects-page.component';
import { canActivateAuth } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: 'projects', component: ProjectsPageComponent },
      { path: 'profile/:id', component: DashboardComponent },
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
