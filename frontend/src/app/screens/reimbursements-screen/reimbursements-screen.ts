import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ReimbursementList } from '../../components/reimbursement-list/reimbursement-list';
import { User } from '../../models/User';
import { UserService } from '../../services/UserService';

@Component({
  selector: 'app-reimbursements-screen',
  imports: [RouterLink, ReimbursementList],
  templateUrl: './reimbursements-screen.html',
  styleUrl: './reimbursements-screen.css',
})
export class ReimbursementsScreen implements OnInit {

  currentUser : User | null = null;
  private cdr = inject(ChangeDetectorRef);

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
  }
}
