package com.bgi.launchpad.model.enums;

/**
 * Event status enumeration for tracking event lifecycle
 */
public enum EventStatus {
    UPCOMING("Upcoming"),
    ONGOING("Ongoing"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    EventStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}