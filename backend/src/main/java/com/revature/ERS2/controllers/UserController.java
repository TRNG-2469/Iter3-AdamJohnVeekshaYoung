package com.revature.ERS2.controllers;

import com.revature.ERS2.dtos.CreateUserDto;
import com.revature.ERS2.dtos.responses.UserResponse;
import com.revature.ERS2.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(required = false) Integer department_id){
        if(department_id != null){
            return ResponseEntity.ok(userService.getUsersByDepartment(department_id));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserDto u) {
        UserResponse createdUser = userService.createUser(u);
        return ResponseEntity.status(201).body(createdUser);
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserResponse> getLoggedInUser(Authentication authentication) {
        String loggedInUsername = authentication.getName();
        return ResponseEntity.ok(userService.getUserByUsername(loggedInUsername));
    }


}
