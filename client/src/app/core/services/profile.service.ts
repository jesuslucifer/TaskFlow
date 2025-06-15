import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable, tap, throwError } from 'rxjs';
export interface IProfile {
  username: string | null;
  email: string | null;
  avatarUrl: string | null;
}
@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  http = inject(HttpClient);
  baseApiUrl = 'http://localhost:8080/api/';
  me = signal<IProfile | null>(null);
  users = signal<IProfile[] | null>(null);
  router = inject(Router);
  getMe() {
    return this.http.get<IProfile>(`${this.baseApiUrl}users/me`).pipe(
      tap((res: IProfile) => this.me.set(res)),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err);
      })
    );
  }
  getProfile(id: string) {
    return this.http.get<IProfile>(`${this.baseApiUrl}users/${id}`);
  }
  uploadImage(file: File): Observable<string> {
    const fd = new FormData();
    fd.append('file', file);
    return this.http
      .post(`${this.baseApiUrl}users/avatar`, fd, {
        responseType: 'text',
      })
      .pipe(
        catchError((err: HttpErrorResponse) => {
          return throwError(() => err);
        })
      );
  }
}
