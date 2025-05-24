import { CookieService } from 'ngx-cookie-service';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { IToken } from '../interface/auth.interface';
import { catchError, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  http = inject(HttpClient);
  accessToken: string | null = null;
  refreshToken: string | null = null;
  router = inject(Router);
  baseApiUrl: string = 'http://localhost:8080/api/auth';
  cookieService = inject(CookieService);
  get isAuth() {
    if (!this.accessToken) {
      this.accessToken = this.cookieService.get('token');
      this.refreshToken = this.cookieService.get('refreshToken');
    }
    return !!this.accessToken;
  }
  signUp(payload: { username: string; password: string; email: string }) {
    return this.http.post(`${this.baseApiUrl}/sign-up`, payload, {
      responseType: 'text',
    });
  }
  login(payload: { usernameOrEmail: string; password: string }) {
    return this.http.post<IToken>(`${this.baseApiUrl}/login`, payload).pipe(
      tap((val) => {
        this.saveTokens(val);
      })
    );
  }
  refreshAuthToken() {
    return this.http
      .post<IToken>(`${this.baseApiUrl}/refresh`, {
        refreshToken: this.refreshToken,
      })
      .pipe(
        tap((val) => {
          this.saveTokens(val);
        }),
        catchError((err) => {
          this.logout();
          return throwError(err);
        })
      );
  }

  saveTokens(res: IToken) {
    this.accessToken = res.accessToken;
    this.refreshToken = res.refreshToken;
    this.cookieService.set('token', this.accessToken);
    this.cookieService.set('refreshToken', this.refreshToken);
  }

  logout() {
    return this.http
      .post(`${this.baseApiUrl}/logout`, null, {
        responseType: 'text',
      })
      .pipe(
        tap(() => {
          this.cookieService.delete('token');
          this.cookieService.delete('refreshToken');
        })
      );
  }
  // logout() {
  //   this.cookieService.delete('token');
  //   this.cookieService.delete('refreshToken');
  //   this.refreshToken = null;
  //   this.accessToken = null;
  //   this.router.navigate(['/login']);
  // }
}
