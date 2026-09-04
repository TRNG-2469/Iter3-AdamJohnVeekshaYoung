package com.revature.ERS2.repositories;

import com.revature.ERS2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    List<User> getUsersByDepartment_DepartmentId(int id);

    boolean existsByUsername(String username);
}
