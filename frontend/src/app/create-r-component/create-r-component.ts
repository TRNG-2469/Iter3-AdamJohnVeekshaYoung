import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CreateReimbursementRequest } from '../dtos/requests/CreateReimbursementRequest';
import { ReimbursementService } from '../services/ReimbursementService';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-create-r-component',
  imports: [FormsModule, MatDialogModule],
  templateUrl: './create-r-component.html',
  styleUrl: './create-r-component.css',
})
export class CreateRComponent {

    constructor(private reimbursementService : ReimbursementService) { }
    
    errorMessage = signal('');
    private dialogRef = inject(MatDialogRef<CreateRComponent>);

    formData : CreateReimbursementRequest = {
      amount: null,
      type: 'OTHER',
      description : ''
    }

    closeModal() {
      this.dialogRef.close();
    }

    submitForm() : void {
      this.reimbursementService.createReimbursement(this.formData).subscribe({
        next: (createdReimbursement) => {
          this.dialogRef.close('created');
        },
        error: (err) => {
          console.log("Failed to create the reimbursement " + err);
          this.errorMessage.set(err.error?.message || 'Failed to create reimbursement');
        }
      }); 
    }
}
