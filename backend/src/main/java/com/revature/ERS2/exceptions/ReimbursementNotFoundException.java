package com.revature.ERS2.exceptions;

public class ReimbursementNotFoundException extends RuntimeException {
    public ReimbursementNotFoundException(int id) {
        super("Reimbursement with Id " + id + " not found.");
    }
}
