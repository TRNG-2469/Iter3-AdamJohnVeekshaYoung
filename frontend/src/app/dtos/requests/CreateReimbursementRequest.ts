import { ReimbursementType } from "../../models/Reimbursement";

export interface CreateReimbursementRequest{
    amount : Number, 
    type : ReimbursementType, 
    description : string
}