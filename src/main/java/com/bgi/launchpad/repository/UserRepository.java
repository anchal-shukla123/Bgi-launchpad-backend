package com.bgi.launchpad.repository;

import com.bgi.launchpad.model.User;
import com.bgi.launchpad.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find by email
    Optional<User> findByEmail(String email);

    // Find active users by role
    Page<User> findByRoleAndIsActiveTrue(Role role, Pageable pageable);

    // Find users by department
    Page<User> findByDepartmentIdAndIsActiveTrue(Long departmentId, Pageable pageable);

    // Check if email exists
    boolean existsByEmail(String email);
}