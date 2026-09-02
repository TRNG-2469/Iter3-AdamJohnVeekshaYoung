package com.revature.ERS2.services;

import com.revature.ERS2.dtos.CreateUserDto;
import com.revature.ERS2.dtos.responses.UserResponse;
import com.revature.ERS2.exceptions.DepartmentNotFoundException;
import com.revature.ERS2.exceptions.UserNotFoundException;
import com.revature.ERS2.models.Department;
import com.revature.ERS2.models.Reimbursement;
import com.revature.ERS2.models.Role;
import com.revature.ERS2.models.User;
import com.revature.ERS2.repositories.DepartmentRepository;
import com.revature.ERS2.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    

    public UserServiceImpl(UserRepository userRepository, DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers(){
        return transformUserToResponse(userRepository.findAll());
    }

    @Override
    public UserResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return transformUserToResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow( () -> new UserNotFoundException(username));

        return transformUserToResponse(user);
    }

    @Override
    public List<UserResponse> getUsersByDepartment(int departmentId) {
        departmentRepository.findById(departmentId).
                orElseThrow( () -> new DepartmentNotFoundException(departmentId));

        return transformUserToResponse(userRepository.getUsersByDepartment_DepartmentId(departmentId));
    }

    @Override
    public UserResponse createUser(CreateUserDto dto) {
        //No Auth because user was just created, redirect to login?

        Department department = departmentRepository
                .findById(dto.getDepartmentId())
                .orElseThrow(() ->
                        new DepartmentNotFoundException(dto.getDepartmentId()));

        User u = new User();
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setUsername(dto.getUsername());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setDepartment(department);
        u.setRole(Role.EMPLOYEE); // explicit default

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User savedUser = userRepository.save(u);
        log.info("User {} created with role '{}'", savedUser.getId(), savedUser.getRole());
        return transformUserToResponse(savedUser);
    }

    /**
     * Helper method to make user objects into responses (cuts password, only returns department id)
     */
    public static UserResponse transformUserToResponse(User u) {

        Department d = u.getDepartment();
        Integer deptId;

        if (d == null) {
            deptId = null;
        } else {
            deptId = d.getDepartmentId();
        }

        return new UserResponse(u.getId(), u.getFirstName(), u.getFirstName(), u.getUsername(),
            u.getRole(), deptId
        );
    }

    /**
     * Transforms list of users to list of UserResponse
     */
    public static List<UserResponse> transformUserToResponse(List<User> userList) {
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User u : userList) {
            userResponseList.add(transformUserToResponse(u));
        }
        return userResponseList;
    }


}
