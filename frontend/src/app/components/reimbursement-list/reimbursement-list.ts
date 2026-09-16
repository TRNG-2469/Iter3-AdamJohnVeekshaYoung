import { Component, OnInit, OnChanges, SimpleChanges, ChangeDetectorRef } from '@angular/core';
import { ReimbursementComponent } from '../reimbursement-component/reimbursement-component';
import { Reimbursement } from '../../models/Reimbursement';
import { Input } from '@angular/core';
import { User } from '../../models/User';
import { ReimbursementService } from '../../services/ReimbursementService';

@Component({
  selector: 'app-reimbursement-list',
  imports: [ReimbursementComponent],
  templateUrl: './reimbursement-list.html',
  styleUrl: './reimbursement-list.css',
})
export class ReimbursementList implements OnInit, OnChanges {

  reimbursements : Reimbursement[] | null = null;

  @Input()
  currentUser! : User;

  @Input()
  filtered_status! : string;

  @Input()
  filtered_department!: string;

  constructor(private reimbursementService : ReimbursementService, private cdr: ChangeDetectorRef) {

  }

  ngOnInit() {
    this.applyFilter();
  }

    ngOnChanges(changes: SimpleChanges): void {
      if (changes['filtered_status'] || changes['filtered_department']){
      this.applyFilter();
    }
  }

    applyFilter() {
    // Convert department string to a number if present, since backend expects Integer
    const deptId = this.filtered_department ? Number(this.filtered_department) : undefined;
    const status = this.filtered_status || undefined;

    //calls the reimbursement service to get reimbursements based on the selected status and department
    this.reimbursementService.getReimbursements(status as any, deptId).subscribe({
      next: (reimbursements) => {
        this.reimbursements = reimbursements;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Failed to fetch filtered reimbursements " + err);
      }
    });

}
}

/* Test data

 /*this.allReimbursements = [
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

      {
        id: 5,
        authorId: 7,
        status: 'PENDING',
        amount: 20.75,
        type: 'TRANSPORTATION',
        description: 'Used a taxi.',
        resolverId: null,
        submittedAt: '2026-08-25T23:03:28.276475',
        resolvedAt: null
      },
    ];
      this.reimbursements=[...this.allReimbursements];

    */
