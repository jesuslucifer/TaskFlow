import { Component } from '@angular/core';
import { LoginComponent } from '../../../auth/login/login.component';

@Component({
  selector: 'app-login-page',
  imports: [LoginComponent],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
})
export class LoginPageComponent {}
