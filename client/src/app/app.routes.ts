import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { RegisterPageComponent } from './pages/authPages/register-page/register-page.component';
import { LoginPageComponent } from './pages/authPages/login-page/login-page.component';
import { ProjectsPageComponent } from './pages/projects-page/projects-page.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: 'profile', component: DashboardComponent },
      { path: 'projects', component: ProjectsPageComponent },
    ],
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
