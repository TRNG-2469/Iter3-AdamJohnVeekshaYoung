package com.revature.ERS2.exceptions;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException(int id) {
        super("Department with id " + id + " not found.");
    }

}
