import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, switchMap, catchError, throwError } from 'rxjs';
import { User } from '../models/User';
import { environment } from '../../environments/environment';


interface LoginRequest {
    username: string;
    password: string;
}
 
interface JwtResponse {
    token: string;
}
 
const TOKEN_KEY = 'ers_token';

@Injectable({
  providedIn: 'root',
})

export class AuthService {

    //todo: put into environment variables 
    private readonly loginUrl = `${environment.apiUrl}/auth/login`;
    private readonly meUrl = `${environment.apiUrl}/users/me`;
 
    // Reactive current-user state. Components read this directly (currentUser())
    // instead of each one re-fetching /users/me on its own.
    currentUser = signal<User | null>(null);
 
    constructor(private http: HttpClient) {
        // Re-hydrate on app startup / page refresh if a token is already stored.
        if (this.getToken()) {
            this.fetchCurrentUser().subscribe();
        }
    }
 
    /**
     * Logs in, stores the JWT, then fetches the full user profile
     * (login only returns a token; /users/me gives us id/role/department
     * which the rest of the app needs).
     */
    login(username: string, password: string): Observable<User> {
        const body: LoginRequest = { username, password };
 
        return this.http.post<JwtResponse>(this.loginUrl, body).pipe(
            tap(res => this.storeToken(res.token)),
            switchMap(() => this.fetchCurrentUser())
        );
    }
 
    logout(): void {
        this.clearToken();
        this.currentUser.set(null);
    }
 
    isAuthenticated(): boolean {
        return !!this.getToken();
    }
 
    getToken(): string | null {
        return localStorage.getItem(TOKEN_KEY);
    }
 
    private fetchCurrentUser(): Observable<User> {
        return this.http.get<User>(this.meUrl).pipe(
            tap(user => this.currentUser.set(user)),
            catchError(err => {
                // Token was rejected (expired/invalid) - clear local state so the
                // app doesn't think we're logged in when the server disagrees.
                this.clearToken();
                this.currentUser.set(null);
                return throwError(() => err);
            })
        );
    } 
 
    private storeToken(token: string): void {
        localStorage.setItem(TOKEN_KEY, token);
    }
 
    private clearToken(): void {
        localStorage.removeItem(TOKEN_KEY);
    }
}
