import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/AuthService';

@Component({
  selector: 'app-reimbursements-screen',
  imports: [RouterLink],
  templateUrl: './reimbursements-screen.html',
  styleUrl: './reimbursements-screen.css',
})
export class ReimbursementsScreen {

  constructor(private authService: AuthService, private router: Router) {}
 
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }


  currentUser:any = {
    id: 1,
    username: "jdoe",
    role: "MANAGER"
  };

  reimbursements:any[] = [
    {
      id: 1,
      authorId: 1,
      status: 'APPROVED',
      amount: 150.00,
      type: 'LODGING',
      description: 'Stayed at a nice hotel.'
    },
    {
      id: 2,
      authorId: 2,
      status: 'DENIED',
      amount: 20.75,
      type: 'FOOD',
      description: 'Had a little snack.'
    },
    {
      id: 3,
      authorId: 1,
      status: 'PENDING',
      amount: 10.50,
      type: 'TRANSPORTATION',
      description: 'Used public bus.'
    },
  ];
}
