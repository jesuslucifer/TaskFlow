import { CommonModule } from '@angular/common';
import { Component, Input, signal } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-auth-form',
  imports: [RouterLink, ReactiveFormsModule, MatIcon, CommonModule],
  templateUrl: './auth-form.component.html',
  styleUrl: './auth-form.component.scss',
})
export class AuthFormComponent {
  @Input() isReg: boolean = false;
  @Input() onSubmit!: () => void;
  @Input() form!: FormGroup;
  isEyeVisible = signal<boolean>(false);
}
