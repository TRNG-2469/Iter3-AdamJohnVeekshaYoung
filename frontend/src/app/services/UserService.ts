// src/app/services/user.service.ts
import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { User } from "../models/User";
import { CreateUserRequest } from "../dtos/requests/CreateUserRequest";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly url = "http://localhost:8081/api/users";
  private readonly authUrl = "http://localhost:8081/api/auth";

  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.url);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.url}/${id}`);
  }

  getCurrentUser(): Observable<User> {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<User>(`${this.url}/me`, { headers });
  }


  createUser(requestBody: CreateUserRequest): Observable<User> {
    return this.http.post<User>(this.url, requestBody);
  }

  login(credentials: { username: string; password: string }): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(`${this.authUrl}/login`, credentials);
  }
}
