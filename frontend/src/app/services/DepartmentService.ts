import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Department } from "../models/Department";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  private readonly url = `${environment.apiUrl}/departments`;

  constructor(private http : HttpClient) {

  }

  getDepartments() : Observable<Department[]> {
    return this.http.get<Department[]>(this.url);
  }

}
