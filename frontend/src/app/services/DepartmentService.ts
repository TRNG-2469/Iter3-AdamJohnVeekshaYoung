import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Department } from "../models/Department";

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  private readonly url = "http://localhost:8081/api/departments";

  constructor(private http : HttpClient) {

  }

  getDepartments() : Observable<Department[]> {
    return this.http.get<Department[]>(this.url);
  }

}
