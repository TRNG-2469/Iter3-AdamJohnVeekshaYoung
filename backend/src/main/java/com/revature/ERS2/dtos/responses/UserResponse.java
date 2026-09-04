package com.revature.ERS2.dtos.responses;

import com.revature.ERS2.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Integer id;

    private String firstName;
    private String lastName;

    private String username;
    private Role role;
    private Integer departmentId;

}
