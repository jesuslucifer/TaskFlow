import { AbstractControl, ValidatorFn } from '@angular/forms';

export function minTodayValidator(): ValidatorFn {
  return (control: AbstractControl) => {
    if (!control.value) return null;

    const today = new Date();
    today.setHours(0, 0, 0, 0); // Убираем время

    const inputDate = new Date(control.value);
    return inputDate < today ? { pastDate: true } : null;
  };
}
