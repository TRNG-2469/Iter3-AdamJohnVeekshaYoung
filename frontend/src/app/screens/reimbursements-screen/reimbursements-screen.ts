import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ReimbursementList } from '../../components/reimbursement-list/reimbursement-list';
import { User } from '../../models/User';
import { UserService } from '../../services/UserService';
import {FormsModule} from '@angular/forms';
import { DepartmentService } from '../../services/DepartmentService';
import { Department } from '../../models/Department';

@Component({
  selector: 'app-reimbursements-screen',
  imports: [RouterLink, ReimbursementList, FormsModule],
  templateUrl: './reimbursements-screen.html',
  styleUrl: './reimbursements-screen.css',
})
export class ReimbursementsScreen implements OnInit {

  currentUser : User | null = null;

  departments: Department[]=[];

  selected_Status: string='';
  filtered_status: string ='';

  selected_Department: string = '';
  filtered_department: string = '';

  private cdr = inject(ChangeDetectorRef);
  private departmentService=inject(DepartmentService);


  constructor(private userService : UserService) {
  }

  //Todo: replace with service call once auth is finished
  ngOnInit() : void {
     /* this.currentUser = {
      id : 1,

      firstName : "jdoe",
      lastName : "jdoe",

      username : "jdoerocks",

      role: "EMPLOYEE",
      departmentId : 1
    } */

    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        console.log('User successfully fetched:', user);
        this.currentUser = user;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('getCurrentUser failed with error:', err);
      }
    });

    this.departmentService.getDepartments().subscribe({

      next: (data) => {
        console.log('Departments response from backend:', data); // Check F12 console to see the exact keys!
        this.departments = data;
        this.cdr.detectChanges()
      },
      error: (err) => {
        console.error('Failed to load departments', err);
      }
    });


  }
  applyFilter() {
    this.filtered_status=this.selected_Status;
    this.filtered_department = this.selected_Department;
  }
}

