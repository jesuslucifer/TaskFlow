import { Component } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CreateFormFieldComponent } from './create-form-field/create-form-field.component';
@Component({
  selector: 'app-create-dialog',
  imports: [MatFormFieldModule, CreateFormFieldComponent],
  templateUrl: './create-dialog.component.html',
  styleUrl: './create-dialog.component.scss',
})
export class CreateDialogComponent {}
