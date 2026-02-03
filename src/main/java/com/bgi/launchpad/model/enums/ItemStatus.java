package com.bgi.launchpad.model.enums;

/**
 * Status enumeration for lost and found items
 */
public enum ItemStatus {
    LOST("Lost"),
    FOUND("Found"),
    CLAIMED("Claimed"),
    EXPIRED("Expired");

    private final String displayName;

    ItemStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}