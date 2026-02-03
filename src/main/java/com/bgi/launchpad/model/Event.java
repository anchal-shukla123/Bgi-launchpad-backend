package com.bgi.launchpad.model;

import com.bgi.launchpad.model.enums.EventStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Event entity for campus events with registration tracking.
 */
@Entity
@Table(name = "events",
       indexes = {
           @Index(name = "idx_event_date", columnList = "event_date"),
           @Index(name = "idx_status", columnList = "status"),
           @Index(name = "idx_committee", columnList = "committee_id")
       })
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotBlank(message = "Venue is required")
    @Size(max = 200, message = "Venue cannot exceed 200 characters")
    @Column(nullable = false, length = 200)
    private String venue;

    @Column(name = "committee_id")
    private Long committeeId;

    @NotNull(message = "Event date is required")
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "registration_deadline")
    private LocalDateTime registrationDeadline;

    @NotNull(message = "Event status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus status = EventStatus.UPCOMING;

    @Min(value = 0, message = "Participant count cannot be negative")
    @Column(name = "current_participants")
    private Integer currentParticipants = 0;

    @Min(value = 1, message = "Maximum participants must be at least 1")
    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "registration_link", length = 500)
    private String registrationLink;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

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

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
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
     * Check if event is full
     */
    public boolean isFull() {
        return maxParticipants != null && currentParticipants >= maxParticipants;
    }

    /**
     * Check if registration is still open
     */
    public boolean isRegistrationOpen() {
        return registrationDeadline == null || LocalDateTime.now().isBefore(registrationDeadline);
    }

    /**
     * Increment participant count
     */
    public void incrementParticipants() {
        if (this.currentParticipants == null) {
            this.currentParticipants = 0;
        }
        this.currentParticipants++;
    }

    /**
     * Validate that end time is after start time
     */
    @AssertTrue(message = "End time must be after start time")
    public boolean isEndTimeValid() {
        return endTime == null || startTime == null || endTime.isAfter(startTime);
    }
}