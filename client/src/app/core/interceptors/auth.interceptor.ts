import {
  HttpErrorResponse,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';
import {
  BehaviorSubject,
  catchError,
  filter,
  switchMap,
  tap,
  throwError,
} from 'rxjs';

let isRefreshing$ = new BehaviorSubject<boolean>(false);

export const AuthTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const accessToken: string | null = authService.accessToken;

  if (!accessToken) return next(req);
  if (isRefreshing$.value) {
    return refreshAndProceed(authService, req, next);
  }
  return next(addToken(req, accessToken)).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 403) {
        return refreshAndProceed(authService, req, next);
      }
      console.error('HTTP Error:', error);
      return throwError(() => error);
    })
  );
};

const refreshAndProceed = (
  authService: AuthService,
  req: HttpRequest<any>,
  next: HttpHandlerFn
) => {
  if (!isRefreshing$.value) {
    isRefreshing$.next(true);
    return authService.refreshAuthToken().pipe(
      switchMap((res) => {
        return next(addToken(req, res.accessToken)).pipe(
          tap(() => {
            isRefreshing$.next(false);
          })
        );
      })
    );
  }
  if (req.url.includes('refresh'))
    return next(addToken(req, authService.refreshToken!));

  return isRefreshing$.pipe(
    filter((isRefreshing) => !isRefreshing),
    switchMap((res) => {
      return next(addToken(req, authService.refreshToken!));
    })
  );
};

const addToken = (req: HttpRequest<any>, accessToken: string) => {
  return req.clone({
    setHeaders: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
};
