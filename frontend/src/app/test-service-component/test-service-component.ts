import { Reimbursement } from '../../models/Reimbursement';
import { ReimbursementService } from '../../services/ReimbursementService';
import { Component } from '@angular/core';


//Test class to test if service returns anything. 
//NOTE TO SELF: CHANGE SECURITY AFTERWARDS 
@Component({
  selector: 'app-test-service-component',
  imports: [],
  templateUrl: './test-service-component.html',
  styleUrl: './test-service-component.css',
})
export class TestServiceComponent {

    reimbursements : Reimbursement[] | null = null; 

    constructor(private rs : ReimbursementService) {

    }

    ngOnInit() : void {
        this.rs.getReimbursements().subscribe(reimbursements => {
          this.reimbursements = reimbursements; 
        });
    }

}
