package com.bgi.launchpad.controller;

import com.bgi.launchpad.dto.request.EventRequestDTO;
import com.bgi.launchpad.dto.response.EventResponseDTO;
import com.bgi.launchpad.model.Event;
import com.bgi.launchpad.service.EventService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*") // Add this if you have CORS issues
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // CREATE EVENT
    @PostMapping
    public ResponseEntity<Event> create(@Valid @RequestBody EventRequestDTO eventRequest) {
        try {
            Event event = eventService.createEvent(eventRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(event);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL EVENTS
    @GetMapping
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    // GET EVENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET EVENTS BY DATE
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Event>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(eventService.getEventsByDate(date));
    }

    // UPDATE EVENT
    @PutMapping("/{id}")
    public ResponseEntity<Event> update(
            @PathVariable Long id,
            @Valid @RequestBody EventRequestDTO eventRequest) {
        try {
            Event updatedEvent = eventService.updateEvent(id, eventRequest);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE EVENT (Soft Delete - marks as deleted)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Event deleted successfully",
                "id", id
            ));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "success", false,
                    "message", e.getMessage()
                ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "message", "Failed to delete event: " + e.getMessage()
                ));
        }
    }

    // HARD DELETE EVENT (Permanently removes from database)
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> permanentDelete(@PathVariable Long id) {
        try {
            eventService.permanentDeleteEvent(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Event permanently deleted",
                "id", id
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "success", false,
                    "message", e.getMessage()
                ));
        }
    }
}