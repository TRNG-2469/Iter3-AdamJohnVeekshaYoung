import { Component, Input } from '@angular/core';
import { Reimbursement } from '../../models/Reimbursement';
import { User } from '../../models/User';
import { Department } from '../../models/Department';
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

  @Input()
  userMap! : Record<number, User>; 

  @Input() 
  departmentMap! : Record<number, Department>;

  ngOnChanges() {
    //console.log(this.userMap);
    console.log(this.departmentMap);
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
