import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthFormComponent } from '../auth-form/auth-form.component';
import { AuthService } from '../../core/services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, AuthFormComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  toastr = inject(ToastrService);
  router = inject(Router);
  authService = inject(AuthService);
  isReg: boolean = true;

  form = new FormGroup({
    username: new FormControl<string | null>(null, [Validators.required]),
    password: new FormControl<string | null>(null, [
      Validators.required,
      Validators.minLength(6),
    ]),
    email: new FormControl<string | null>(null, [
      Validators.required,
      Validators.email,
    ]),
  });
  onSubmit = () => {
    if (this.form.valid) {
      //@ts-ignore
      this.authService.signUp(this.form.value).subscribe({
        next: () => {
          this.router.navigate(['/login']);
          this.toastr.success('Вы успешно зарегистрировались');
        },
        error: (err: HttpErrorResponse) => {
          this.toastr.error(err.error.message);
        },
      });
    } else {
      this.toastr.error('Введите корректные данные');
    }
  };
}
