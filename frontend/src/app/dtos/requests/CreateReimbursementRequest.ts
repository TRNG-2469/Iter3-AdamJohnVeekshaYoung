import { ReimbursementType } from "../../models/Reimbursement";

export interface CreateReimbursementRequest{
    amount : Number | null, 
    type : ReimbursementType, 
    description : string
}