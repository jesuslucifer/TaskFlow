import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthFormComponent } from '../auth-form/auth-form.component';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, AuthFormComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  router = inject(Router);
  isReg: boolean = true;

  form = new FormGroup({
    username: new FormControl(null),
    email: new FormControl(null),
    password: new FormControl(null),
  });
  onSubmit = () => {
    console.log(this.form.value);

    this.router.navigate(['']);
  };
}
