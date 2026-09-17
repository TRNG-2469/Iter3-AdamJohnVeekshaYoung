import { Component, Input, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { Reimbursement } from '../../models/Reimbursement';
import { User } from '../../models/User';
import { CurrencyPipe} from '@angular/common';
import { ReimbursementService } from '../../services/ReimbursementService';
import { EditReimbursementRequest } from '../../dtos/requests/EditReimbursement';
import { ReimbursementType } from '../../models/Reimbursement';
import { FormsModule } from '@angular/forms';
import { Department } from '../../models/Department';

@Component({
  selector: 'tr[app-reimbursement-component]',
  imports: [CurrencyPipe, FormsModule],
  templateUrl: './reimbursement-component.html',
  styleUrl: './reimbursement-component.css',
})
export class ReimbursementComponent {

  @Input()
  r! : Reimbursement;

  @Input()
  currentUser! : User;

  @Input()
  userMap! : Record<number, User>;

  @Input()
  departmentMap! : Record<number, Department>;

  @Output()
  reimbursementUpdated = new EventEmitter<Reimbursement>();

  //Emitted after a reimbursement status is successfully resolved, so the list can refresh
  @Output()
  resolved = new EventEmitter<void>();

  //Emitted after a reimbursement is successfully deleted, so the list can refresh
  @Output()
  deleted = new EventEmitter<void>();

  editMode : boolean = false;

  newAmount : Number = 0;
  newType : ReimbursementType = 'OTHER';
  newDescription : string | null = null;

  editError : string = '';

  constructor(private reimbursementService : ReimbursementService, private cdr: ChangeDetectorRef) {

  }

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

  activateEditMode(): void {
    this.newAmount = this.r.amount;
    this.newType = this.r.type;
    this.newDescription = this.r.description;
    this.editError = '';
    this.editMode = true;
  }

  deactivateEditMode(): void {
    this.editMode = false;
    this.editError = '';
  }

  saveChanges(): void {
    const amount = Number(this.newAmount);

    if (amount >= 1000000 || amount < 0) {
      this.editError = 'Value cannot be greater than 1,000,000 or less than 0';
      return;
    }

    this.editError = '';

    const requestBody : EditReimbursementRequest = {
        amount : this.newAmount,
        type : this.newType,
        description : this.newDescription
      }
    this.reimbursementService.editReimbursement(this.r.id, requestBody).subscribe(
    {
      next: (reimbursement) => {
        //this.r = reimbursement;
        this.reimbursementUpdated.emit(reimbursement);
        this.deactivateEditMode();
      },
      error: (err) => {
        console.error("Failed to save reimbursement " + err);
        this.editError = err.error?.amount
          ? 'Value cannot be greater than 1,000,000 or less than 0'
          : 'Failed to save changes';
      }
    });
  }

  onAmountChange(value: number): void {
    this.newAmount = Math.round(value * 100) / 100;
  }

  get author() : User | undefined {
    return this.userMap[this.r.authorId];
  }

  get department() : Department | undefined {
    if (!this.author) {
      return undefined;
    }

    return this.departmentMap[this.author.departmentId];
  }

}
