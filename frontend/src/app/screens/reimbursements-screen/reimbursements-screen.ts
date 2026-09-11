import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ReimbursementList } from '../../components/reimbursement-list/reimbursement-list';
import { User } from '../../../models/User';
import { UserService } from '../../../services/UserService';

@Component({
  selector: 'app-reimbursements-screen',
  imports: [RouterLink, ReimbursementList],
  templateUrl: './reimbursements-screen.html',
  styleUrl: './reimbursements-screen.css',
})
export class ReimbursementsScreen {

  currentUser : User | null = null;

  constructor(private userService : UserService) {
  }

  //Todo: replace with service call once auth is finished 
  ngOnInit() : void {
      this.currentUser = {
      id : 1, 

      firstName : "jdoe", 
      lastName : "jdoe", 

      username : "jdoerocks", 

      role: "EMPLOYEE",
      departmentId : 1
    }
    
    /* TODO: make a better error 
      this.userService.getCurrentUser().subscribe({
        next: (user) => { 
          this.currentUser = user;
        }, 
        
        error: (err) => {
          console.log("failed to fetch user with error message: " + err);
        }
      })
    */
  }
}
