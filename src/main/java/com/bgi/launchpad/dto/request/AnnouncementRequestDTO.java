package com.bgi.launchpad.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating/updating announcements.
 * Used to receive announcement data from the client.
 */
public class AnnouncementRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    private String description;

    private Long departmentId; // null means "All Departments"

    private Boolean hasPoll = false;

    // Constructors
    public AnnouncementRequestDTO() {}

    public AnnouncementRequestDTO(String title, String description, Long departmentId, Boolean hasPoll) {
        this.title = title;
        this.description = description;
        this.departmentId = departmentId;
        this.hasPoll = hasPoll;
    }

    // Getters and Setters
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

    public Boolean getHasPoll() {
        return hasPoll;
    }

    public void setHasPoll(Boolean hasPoll) {
        this.hasPoll = hasPoll;
    }
}