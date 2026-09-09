//This is the shape of ReimbursementResponse which is returned from our endpoints
//Can consider maybe modeling author with User obj instead of id
export interface Reimbursement {
    id: number, 
    authorId: number, 
    resolverId: number | null, 
    amount: number, 

    status: ReimbursementStatus,
    type: ReimbursementType,

    description: string | null, 

    submittedAt: string,
    resolvedAt: string | null 
}

export type ReimbursementStatus = 'PENDING' | 'APPROVED' | 'DENIED'; 
export type ReimbursementType = 'TRAVEL' | 'FOOD' | 'LODGING' | 'MEDICAL' | 'TRANSPORTATION' | 'OTHER';
