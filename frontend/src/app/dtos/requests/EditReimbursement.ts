import { ReimbursementType } from "../../models/Reimbursement";

export interface EditReimbursementRequest {
    amount : Number, 
    type : ReimbursementType, 
    description : string | null
}