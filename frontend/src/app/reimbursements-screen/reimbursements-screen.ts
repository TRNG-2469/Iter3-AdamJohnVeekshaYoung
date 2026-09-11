import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UserService } from '../../services/UserService';

@Component({
  selector: 'app-reimbursements-screen',
  imports: [RouterLink],
  templateUrl: './reimbursements-screen.html',
  styleUrl: './reimbursements-screen.css',
})

export class ReimbursementsScreen implements OnInit {
  private userService = inject(UserService);
  private cdr = inject(ChangeDetectorRef);

  currentUser: any = null;

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

  ngOnInit(): void {
    console.log('Fetching current user profile...');

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
