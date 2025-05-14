import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthFormComponent } from '../auth-form/auth-form.component';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, AuthFormComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  router = inject(Router);
  isReg: boolean = false;

  form = new FormGroup({
    email: new FormControl(null),
    password: new FormControl(null),
  });
  onSubmit() {
    this.router.navigate(['']);
    console.log(this.form.value);
  }
}
