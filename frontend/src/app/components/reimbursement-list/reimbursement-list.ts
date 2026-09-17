import { Component, OnInit, OnChanges, SimpleChanges, signal } from '@angular/core';
import { ReimbursementComponent } from '../reimbursement-component/reimbursement-component';
import { Reimbursement } from '../../models/Reimbursement';
import { Input } from '@angular/core';
import { User } from '../../models/User';
import { ReimbursementService } from '../../services/ReimbursementService';
import { Department } from '../../models/Department';

@Component({
  selector: 'app-reimbursement-list',
  imports: [ReimbursementComponent],
  templateUrl: './reimbursement-list.html',
  styleUrl: './reimbursement-list.css',
})
export class ReimbursementList implements OnInit, OnChanges {

  reimbursements = signal<Reimbursement[]>([]);

  @Input()
  currentUser! : User;

  @Input()
  filtered_status! : string;

  @Input()
  filtered_department!: string;

  //shows reimbursement history instead of the filtered list when true
  @Input()
  historyMode : boolean = false;

  @Input()
  userMap!: Record<number, User>

  @Input()
  departmentMap!: Record<number, Department>

  constructor(private reimbursementService : ReimbursementService) { }

  ngOnInit() {
    this.applyFilter();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['filtered_status'] || changes['filtered_department'] || changes['historyMode']){
      this.applyFilter();
    }
  }

  applyFilter() {

    if (this.historyMode) {
      this.reimbursementService.getReimbursementHistory().subscribe({
        next: (reimbursementData) => {
          this.reimbursements.set(reimbursementData);
        },
        error: (err) => {
          console.error("Failed to fetch reimbursement history " + err);
        }
      });
      return;
    }

    // Convert department string to a number if present, since backend expects Integer
    const deptId = this.filtered_department ? Number(this.filtered_department) : undefined;
    const status = this.filtered_status || undefined;

    //calls the reimbursement service to get reimbursements based on the selected status and department
    this.reimbursementService.getReimbursements(status as any, deptId).subscribe({
      next: (reimbursementData) => {
        this.reimbursements.set(reimbursementData);
      },
      error: (err) => {
        console.error("Failed to fetch filtered reimbursements " + err);
      }
    });
  }

  onReimbursementUpdated(updated: Reimbursement): void {
    this.reimbursements.update(list =>
      list.map(r => r.id === updated.id ? updated : r)
    );
  }

}
