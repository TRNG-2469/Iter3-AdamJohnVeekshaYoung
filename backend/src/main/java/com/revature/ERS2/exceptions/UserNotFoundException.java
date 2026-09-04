package com.revature.ERS2.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("User with Id " + id + " not found.");
    }

    public UserNotFoundException(String username) {
        super("User with username " + username + " not found.");
    }
}
