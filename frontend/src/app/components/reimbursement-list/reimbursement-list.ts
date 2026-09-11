import { Component } from '@angular/core';
import { ReimbursementComponent } from '../reimbursement-component/reimbursement-component';
import { Reimbursement } from '../../../models/Reimbursement';
import { Input } from '@angular/core';
import { User } from '../../../models/User';
import { ReimbursementService } from '../../../services/ReimbursementService';

@Component({
  selector: 'app-reimbursement-list',
  imports: [ReimbursementComponent],
  templateUrl: './reimbursement-list.html',
  styleUrl: './reimbursement-list.css',
})
export class ReimbursementList {

  reimbursements : Reimbursement[] | null = null; 
  
  @Input()
  currentUser! : User;  

  constructor(private reimbursementService : ReimbursementService) {

  }

  //Todo: replace with service once auth finished 
  ngOnInit() {
    this.reimbursements = [
    {
      id: 1,
      authorId: 1,
      status: 'APPROVED',
      amount: 150.00,
      type: 'LODGING',
      description: 'Stayed at a nice hotel.',
      resolverId: null, 
      submittedAt: '2026-08-25T22:06:01.951029',
      resolvedAt: null
    },
    {
      id: 2,
      authorId: 2,
      status: 'DENIED',
      amount: 20.75,
      type: 'FOOD',
      description: 'Had a little snack.',
      resolverId: null, 
      submittedAt: '2026-08-26T16:54:48.857607',
      resolvedAt: null
    },
    {
      id: 3,
      authorId: 1,
      status: 'PENDING',
      amount: 10.50,
      type: 'TRANSPORTATION',
      description: 'Used public bus.',
      resolverId: null, 
      submittedAt: '2026-08-25T23:03:28.276475',
      resolvedAt: null
    },
  ];

  /*
    this.reimbursementService.getReimbursements().subscribe({
      next: (reimbursements) => {
        this.reimbursements = reimbursements;
      },
      error: (err) => {
        console.log("failed to fetch reimbursements " + err);
      }
    })
  */

}
}