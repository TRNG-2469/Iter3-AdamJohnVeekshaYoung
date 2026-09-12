import { Component, Input } from '@angular/core';
import { Reimbursement } from '../../models/Reimbursement';
import { User } from '../../models/User';
import { CurrencyPipe } from '@angular/common';

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

}
