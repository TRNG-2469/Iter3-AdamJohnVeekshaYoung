export interface User {
    id : number, 

    firstName : string, 
    lastName : string, 

    username : string, 

    role: UserRole,
    departmentId : number 
}

export type UserRole = "EMPLOYEE" | "MANAGER";