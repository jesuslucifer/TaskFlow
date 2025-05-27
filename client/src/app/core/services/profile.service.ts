import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';
export interface IProfile {
  username: string;
  email: string;
  avatarUrl: string;
}
@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  http = inject(HttpClient);
  baseApiUrl = 'http://localhost:8080/api/';
  // me = signal<IProfile | null>(null);
  getMe(): Observable<IProfile> {
    return this.http.get<IProfile>(`${this.baseApiUrl}user/me`);
    // .pipe(tap((res: IProfile) => this.me.set(res)));
  }
  constructor() {}
}
