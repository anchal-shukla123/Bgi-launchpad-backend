package com.bgi.launchpad.model;

import com.bgi.launchpad.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * User entity representing students, faculty, and administrators.
 * 
 * IMPROVEMENTS from original:
 * - Added validation annotations
 * - Using Role enum instead of String
 * - Password field with encryption
 * - Indexed email for faster lookups
 * - Extends BaseEntity for audit fields
 * - Added proper constraints
 */
@Entity
@Table(name = "users", 
       indexes = {
           @Index(name = "idx_email", columnList = "email"),
           @Index(name = "idx_department_role", columnList = "department_id, role")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_email", columnNames = "email")
       })
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 60, max = 60, message = "Password hash must be 60 characters")
    @Column(nullable = false, length = 60)
    private String password; // BCrypt hashed password

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Constructors
    public User() {}

    public User(String name, String email, String password, Role role, Long departmentId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.departmentId = departmentId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}