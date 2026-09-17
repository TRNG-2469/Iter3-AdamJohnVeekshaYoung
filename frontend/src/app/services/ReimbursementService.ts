import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Reimbursement, ReimbursementStatus } from "../models/Reimbursement";
import { CreateReimbursementRequest } from "../dtos/requests/CreateReimbursementRequest";
import { EditReimbursementRequest } from "../dtos/requests/EditReimbursement";
import { ResolveReimbursementRequest } from "../dtos/requests/ResolveReimbursementRequest";
import { environment } from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ReimbursementService {

    private readonly url = `${environment.apiUrl}/reimbursements`;

    constructor(private http : HttpClient) {};

    //Pass in optional status, departmentId
    //If manager calls, gets all the reimbursements of all users
    getReimbursements(status? : ReimbursementStatus, departmentId? : number) : Observable<Reimbursement[]> {

        let params = new HttpParams();

        if (status !== undefined) {
            params = params.set('status', status);
        }

        //I think we don't need to check auth here? Should be handled by token in backend, other components in frontend
        if (departmentId !== undefined) {
            params = params.set('departmentId', departmentId);
        }

      const token = localStorage.getItem('authToken');

      // Build headers with the Bearer token
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });

      // Pass both headers and params into the request
      return this.http.get<Reimbursement[]>(this.url, { headers, params });
    }

    //Manager calls to get their own reimbursements
    getManagerOwnedReimbursements() : Observable<Reimbursement[]> {
        return this.http.get<Reimbursement[]>(`${this.url}/own`);
    }

    getReimbursementById(rId : number) : Observable<Reimbursement> {
        return this.http.get<Reimbursement>(`${this.url}/${rId}`);
    }

    getReimbursementHistory() : Observable<Reimbursement[]> {
        return this.http.get<Reimbursement[]>(`${this.url}/history`);
    }

    createReimbursement(requestBody : CreateReimbursementRequest) : Observable<Reimbursement> {
        return this.http.post<Reimbursement>(this.url, requestBody);
    }

    editReimbursement(id: number, requestBody : EditReimbursementRequest) : Observable<Reimbursement> {
        return this.http.patch<Reimbursement>(`${this.url}/${id}`, requestBody);
    }

    resolveReimbursement(id: number, requestBody : ResolveReimbursementRequest) : Observable<Reimbursement> {
        return this.http.patch<Reimbursement>(`${this.url}/${id}/status`, requestBody);
    }

    deleteReimbursementById(id: number) : Observable<void>{
        return this.http.delete<void>(`${this.url}/${id}`);
    }
}
