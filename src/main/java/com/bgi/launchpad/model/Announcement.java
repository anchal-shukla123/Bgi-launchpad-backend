// package com.bgi.launchpad.model;

// import jakarta.persistence.*;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Size;

// /**
//  * Announcement entity for campus-wide and department-specific announcements.
//  * 
//  * IMPROVEMENTS from original:
//  * - Added validation annotations
//  * - Indexed departmentId for faster queries
//  * - Extends BaseEntity for audit fields
//  * - Added view count tracking
//  * - Added comment count
//  * - Proper column definitions
//  * - Added foreign key relationships
//  */
// @Entity
// @Table(name = "announcements",
//        indexes = {
//            @Index(name = "idx_department", columnList = "department_id"),
//            @Index(name = "idx_created_at", columnList = "created_at")
//        })
// public class Announcement extends BaseEntity {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @NotBlank(message = "Title is required")
//     @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
//     @Column(nullable = false, length = 200)
//     private String title;

//     @NotBlank(message = "Description is required")
//     @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
//     @Column(columnDefinition = "TEXT", nullable = false)
//     private String description;

//     @Column(name = "department_id")
//     private Long departmentId; // Null means "All Departments"

//     @NotNull(message = "User ID is required")
//     @Column(name = "user_id", nullable = false)
//     private Long userId;

//     @Column(name = "view_count")
//     private Long viewCount = 0L;

//     @Column(name = "comment_count")
//     private Integer commentCount = 0;

//     @Column(name = "has_poll")
//     private Boolean hasPoll = false;

//     // Getters and Setters
//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public String getTitle() {
//         return title;
//     }

//     public void setTitle(String title) {
//         this.title = title;
//     }

//     public String getDescription() {
//         return description;
//     }

//     public void setDescription(String description) {
//         this.description = description;
//     }

//     public Long getDepartmentId() {
//         return departmentId;
//     }

//     public void setDepartmentId(Long departmentId) {
//         this.departmentId = departmentId;
//     }

//     public Long getUserId() {
//         return userId;
//     }

//     public void setUserId(Long userId) {
//         this.userId = userId;
//     }

//     public Long getViewCount() {
//         return viewCount;
//     }

//     public void setViewCount(Long viewCount) {
//         this.viewCount = viewCount;
//     }

//     public Integer getCommentCount() {
//         return commentCount;
//     }

//     public void setCommentCount(Integer commentCount) {
//         this.commentCount = commentCount;
//     }

//     public Boolean getHasPoll() {
//         return hasPoll;
//     }

//     public void setHasPoll(Boolean hasPoll) {
//         this.hasPoll = hasPoll;
//     }

//     /**
//      * Increments view count by 1
//      */
//     public void incrementViewCount() {
//         this.viewCount = (this.viewCount == null ? 0 : this.viewCount) + 1;
//     }

//     /**
//      * Increments comment count by 1
//      */
//     public void incrementCommentCount() {
//         this.commentCount = (this.commentCount == null ? 0 : this.commentCount) + 1;
//     }
// }

package com.bgi.launchpad.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Announcement entity for campus-wide and department-specific announcements.
 */
@Entity
@Table(name = "announcements",
       indexes = {
           @Index(name = "idx_department", columnList = "department_id"),
           @Index(name = "idx_created_at", columnList = "created_at")
       })
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "department_id")
    private Long departmentId; // Null means "All Departments"

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @Column(name = "has_poll")
    private Boolean hasPoll = false;

    // Audit fields (if BaseEntity is not available)
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Boolean getHasPoll() {
        return hasPoll;
    }

    public void setHasPoll(Boolean hasPoll) {
        this.hasPoll = hasPoll;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * Increments view count by 1
     */
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0 : this.viewCount) + 1;
    }

    /**
     * Increments comment count by 1
     */
    public void incrementCommentCount() {
        this.commentCount = (this.commentCount == null ? 0 : this.commentCount) + 1;
    }
}