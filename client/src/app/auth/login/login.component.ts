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
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, AuthFormComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  toastr = inject(ToastrService);
  router = inject(Router);
  isReg: boolean = false;
  authService = inject(AuthService);
  form: FormGroup = new FormGroup({
    usernameOrEmail: new FormControl<string | null>(null, [
      Validators.required,
    ]),
    password: new FormControl<string | null>(null, [
      Validators.required,
      Validators.minLength(6),
    ]),
  });
  onSubmit = () => {
    if (this.form.valid) {
      this.authService.login(this.form.value).subscribe({
        next: () => {
          this.router.navigate(['/profile/me']);
          this.toastr.success('Вы успешно авторизовались');
        },
        error: (err) => {
          this.toastr.error(err.error.message);
        },
      });
    } else {
      this.toastr.warning('Введите корректные данные');
    }
  };
}
