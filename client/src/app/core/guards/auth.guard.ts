import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

export const canActivateAuth = () => {
  const isLogIn = inject(AuthService).isAuth;
  if (isLogIn) {
    return true;
  }

  return inject(Router).createUrlTree(['/login']);
};
