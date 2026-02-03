package com.bgi.launchpad.model.enums;

/**
 * User roles in the system.
 * STUDENT: Regular student with view-only access
 * FACULTY: Faculty member with limited admin access
 * HOD: Head of Department with full admin access
 */
public enum Role {
    STUDENT("Student"),
    FACULTY("Faculty"),
    HOD("Head of Department"),
    ADMIN("Administrator");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}