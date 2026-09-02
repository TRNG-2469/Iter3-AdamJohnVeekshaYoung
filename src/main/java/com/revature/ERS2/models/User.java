package com.revature.ERS2.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer id;

    @NotBlank(message = "Last Name is mandatory")
    @Size(max = 50, message = "Last Name must be less than 50 characters")
    private String lastName;

    @NotBlank(message = "First Name is mandatory")
    @Size(max = 50, message = "First Name must be less than 50 characters")
    private String firstName;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 30, message = "Username must be less than 30 characters")
    private String username;

    @Column(name="hashed_password")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading optimizes database queries
    @JoinColumn(name = "department_id")
    private Department department;
}
