package com.revature.ERS2.services;

import com.revature.ERS2.dtos.CreateUserDto;
import com.revature.ERS2.dtos.responses.UserResponse;
import com.revature.ERS2.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponse getUserById(Integer userId);
    UserResponse getUserByUsername(String username);

    List<UserResponse> getAllUsers();

    List<UserResponse> getUsersByDepartment(int departmentId);

    //Department has multiple users, changed to list

    UserResponse createUser(CreateUserDto u);

}
