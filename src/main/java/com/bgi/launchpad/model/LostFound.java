package com.bgi.launchpad.model;

import com.bgi.launchpad.model.enums.ItemStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * LostFound entity for tracking lost and found items on campus.
 * 
 * IMPROVEMENTS from original:
 * - Added validation annotations
 * - Using ItemStatus enum for type safety
 * - Added expiration date tracking
 * - Added image URL support
 * - Better indexing for searches
 * - Extends BaseEntity for audit fields
 * - Added contact information
 */
@Entity
@Table(name = "lost_found",
       indexes = {
           @Index(name = "idx_status", columnList = "status"),
           @Index(name = "idx_category", columnList = "category"),
           @Index(name = "idx_created_at", columnList = "created_at")
       })
public class LostFound extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name is required")
    @Size(min = 3, max = 200, message = "Item name must be between 3 and 200 characters")
    @Column(name = "item_name", nullable = false, length = 200)
    private String itemName;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category cannot exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String category; // Personal Items, Electronics, Books, Accessories, Other

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location cannot exceed 200 characters")
    @Column(nullable = false, length = 200)
    private String location;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemStatus status = ItemStatus.LOST;

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "contact_info", length = 200)
    private String contactInfo;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt; // Auto-expire after 30 days

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Set expiration date to 30 days from creation
     */
    @PrePersist
    protected void setExpirationDate() {
        if (expiresAt == null) {
            expiresAt = LocalDateTime.now().plusDays(30);
        }
    }

    /**
     * Check if item has expired
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Get days until expiration
     */
    public long getDaysUntilExpiration() {
        if (expiresAt == null) {
            return 0;
        }
        return java.time.Duration.between(LocalDateTime.now(), expiresAt).toDays();
    }
}