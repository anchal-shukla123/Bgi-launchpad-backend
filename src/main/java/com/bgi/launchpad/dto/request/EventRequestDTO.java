package com.bgi.launchpad.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for creating/updating events.
 */
public class EventRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    @NotBlank(message = "Venue is required")
    @Size(max = 200, message = "Venue cannot exceed 200 characters")
    private String venue;

    private Long committeeId;

    @NotNull(message = "Event date is required")
    @FutureOrPresent(message = "Event date must be today or in the future")
    private LocalDate eventDate;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private LocalDateTime registrationDeadline;

    @Min(value = 1, message = "Maximum participants must be at least 1")
    private Integer maxParticipants;

    private String registrationLink;

    private String imageUrl;

    // Constructors
    public EventRequestDTO() {}

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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Long getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(Long committeeId) {
        this.committeeId = committeeId;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(LocalDateTime registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getRegistrationLink() {
        return registrationLink;
    }

    public void setRegistrationLink(String registrationLink) {
        this.registrationLink = registrationLink;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}