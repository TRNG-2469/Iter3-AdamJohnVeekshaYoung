import { Component, OnInit, inject, signal, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ReimbursementList } from '../../components/reimbursement-list/reimbursement-list';
import { User } from '../../models/User';
import { UserService } from '../../services/UserService';
import {FormsModule} from '@angular/forms';
import { DepartmentService } from '../../services/DepartmentService';
import { Department } from '../../models/Department';
import { CreateRComponent } from '../../create-r-component/create-r-component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-reimbursements-screen',
  imports: [RouterLink, ReimbursementList, FormsModule],
  templateUrl: './reimbursements-screen.html',
  styleUrl: './reimbursements-screen.css',
})
export class ReimbursementsScreen implements OnInit {

  //Since we already created state (reimbursements) for list to control, we have to use viewchild to 
  //manually trigger the refresh (could also move state up)
  @ViewChild(ReimbursementList) 
  reimbursementList! : ReimbursementList;

  //Changed state to signals, passed to children as instances of models 
  currentUser = signal<User | null>(null);
  departments = signal<Department[]>([]);
  
  selected_Status: string='';
  filtered_status: string ='';

  selected_Department: string = '';
  filtered_department: string = '';

  viewingHistory = signal(false);

  private departmentService=inject(DepartmentService);
  private dialog = inject(MatDialog);

  constructor(private userService : UserService) {
  }

  //Todo: replace with service call once auth is finished
  ngOnInit() : void {

    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        console.log('User successfully fetched:', user);
        this.currentUser.set(user);
      },
      error: (err) => {
        console.error('getCurrentUser failed with error:', err);
      }
    });

    this.departmentService.getDepartments().subscribe({
      next: (data) => {
        console.log('Departments response from backend:', data); 
        this.departments.set(data);
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

  toggleHistory() {
    this.viewingHistory.update(viewingHistory => !viewingHistory);
  }

  openModal() { 
    console.log("opened modal"); 
    const dialogRef = this.dialog.open(CreateRComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'created') {
        //as of right now, we refresh the reimbursements by calling applyFilter() 
        this.reimbursementList.applyFilter();
      }
    })
  }

}
