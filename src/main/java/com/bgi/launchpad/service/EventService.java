
package com.bgi.launchpad.service;

import com.bgi.launchpad.dto.request.EventRequestDTO;
import com.bgi.launchpad.model.Event;
import com.bgi.launchpad.model.enums.EventStatus;
import com.bgi.launchpad.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Create event from DTO
    @Transactional
    public Event createEvent(EventRequestDTO dto) {
        Event event = new Event();
        
        // Map DTO fields to Entity
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setVenue(dto.getVenue());
        event.setCommitteeId(dto.getCommitteeId());
        event.setEventDate(dto.getEventDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setRegistrationDeadline(dto.getRegistrationDeadline());
        event.setMaxParticipants(dto.getMaxParticipants() != null ? dto.getMaxParticipants() : 100);
        event.setCurrentParticipants(0);
        event.setRegistrationLink(dto.getRegistrationLink());
        event.setImageUrl(dto.getImageUrl());
        
        // Set default status as UPCOMING
        event.setStatus(EventStatus.UPCOMING);
        
        return eventRepository.save(event);
    }

    // Get all events (excluding soft-deleted ones)
    public List<Event> getAllEvents() {
        return eventRepository.findAll().stream()
                .filter(event -> event.getIsDeleted() == null || !event.getIsDeleted())
                .toList();
    }

    // Get event by ID
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id)
                .filter(event -> event.getIsDeleted() == null || !event.getIsDeleted());
    }

    // Get events by date
    public List<Event> getEventsByDate(LocalDate date) {
        return eventRepository.findByEventDate(date).stream()
                .filter(event -> event.getIsDeleted() == null || !event.getIsDeleted())
                .toList();
    }
    
    // Update event
    @Transactional
    public Event updateEvent(Long id, EventRequestDTO dto) {
        Event event = eventRepository.findById(id)
                .filter(e -> e.getIsDeleted() == null || !e.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setVenue(dto.getVenue());
        event.setCommitteeId(dto.getCommitteeId());
        event.setEventDate(dto.getEventDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setRegistrationDeadline(dto.getRegistrationDeadline());
        event.setMaxParticipants(dto.getMaxParticipants());
        event.setRegistrationLink(dto.getRegistrationLink());
        event.setImageUrl(dto.getImageUrl());
        
        return eventRepository.save(event);
    }
    
    // Soft delete event (marks as deleted but keeps in database)
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        // Check if already deleted
        if (event.getIsDeleted() != null && event.getIsDeleted()) {
            throw new RuntimeException("Event is already deleted");
        }
        
        // Soft delete - just mark as deleted
        event.setIsDeleted(true);
        eventRepository.save(event);
    }
    
    // Permanently delete event (removes from database)
    @Transactional
    public void permanentDeleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        eventRepository.delete(event);
    }
    
    // Restore soft-deleted event
    @Transactional
    public Event restoreEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        if (event.getIsDeleted() == null || !event.getIsDeleted()) {
            throw new RuntimeException("Event is not deleted");
        }
        
        event.setIsDeleted(false);
        return eventRepository.save(event);
    }
}