import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { User } from "../models/User";
import { CreateUserRequest } from "../dtos/requests/CreateUserRequest";

@Injectable({
    providedIn: 'root'
})
export class UserService {
    
    private readonly url = "http://localhost:8081/api/users";
    
    constructor(private http : HttpClient) {}

    getUsers() : Observable<User[]>{
        return this.http.get<User[]>(this.url);
    }

    getUserById(id : number) : Observable<User> {
        return this.http.get<User>(`${this.url}/${id}`);
    }

    getCurrentUser() : Observable<User> {
        return this.http.get<User>(`${this.url}/me`);
    }

    createUser(requestBody : CreateUserRequest) : Observable<User> {
        return this.http.post<User>(this.url, requestBody);
    }


}