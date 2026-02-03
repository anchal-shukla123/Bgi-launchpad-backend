package com.bgi.launchpad.dto.request;

import com.bgi.launchpad.model.enums.ItemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating/updating lost & found items.
 */
public class LostFoundRequestDTO {

    @NotBlank(message = "Item name is required")
    @Size(min = 3, max = 200, message = "Item name must be between 3 and 200 characters")
    private String itemName;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    @NotBlank(message = "Category is required")
    private String category; // Personal Items, Electronics, Books, Accessories, Other

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location cannot exceed 200 characters")
    private String location;

    @NotNull(message = "Status is required")
    private ItemStatus status; // LOST or FOUND

    private String imageUrl;

    private String contactInfo;

    // Constructors
    public LostFoundRequestDTO() {}

    // Getters and Setters
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
}