import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Reimbursement } from '../../models/Reimbursement';
import { User } from '../../models/User';
import { CurrencyPipe } from '@angular/common';
import { ReimbursementService } from '../../services/ReimbursementService';


@Component({
  selector: 'tr[app-reimbursement-component]',
  imports: [CurrencyPipe],
  templateUrl: './reimbursement-component.html',
  styleUrl: './reimbursement-component.css',
})
export class ReimbursementComponent {

  @Input()
  r! : Reimbursement;

  @Input() 
  currentUser! : User;

  //Emitted after a reimbursement status is successfully resolved, so the list can refresh
  @Output()
  resolved = new EventEmitter<void>();

  //Emitted after a reimbursement is successfully deleted, so the list can refresh
  @Output()
  deleted = new EventEmitter<void>();

  constructor(private reimbursementService : ReimbursementService) { }

  approve() {
    this.resolve('APPROVED');
  }

  deny() {
    this.resolve('DENIED');
  }

  private resolve(status : 'APPROVED' | 'DENIED') {
    this.reimbursementService.resolveReimbursement(this.r.id, { status }).subscribe({
      next: () => {
        this.resolved.emit();
      },
      error: (err) => {
        console.error(`Failed to ${status === 'APPROVED' ? 'approve' : 'deny'} reimbursement ${this.r.id}`, err);
      }
    });
  }

  delete() {
    this.reimbursementService.deleteReimbursementById(this.r.id).subscribe({
      next: () => {
        this.deleted.emit();
      },
      error: (err) => {
        console.error(`Failed to delete reimbursement ${this.r.id}`, err);
      }
    });
  }
}
