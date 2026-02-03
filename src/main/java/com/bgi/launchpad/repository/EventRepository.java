package com.bgi.launchpad.repository;

import com.bgi.launchpad.model.Announcement;
import com.bgi.launchpad.model.Event;
import com.bgi.launchpad.model.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Find all non-deleted events
    Page<Event> findAllByIsDeletedFalse(Pageable pageable);

    // Find by status
    Page<Event> findByStatusAndIsDeletedFalse(EventStatus status, Pageable pageable);

    // Find by date range
    @Query("SELECT e FROM Event e WHERE e.eventDate BETWEEN :startDate AND :endDate AND e.isDeleted = false")
    Page<Event> findByEventDateBetween(@Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate, 
                                       Pageable pageable);

    // Find upcoming events
    @Query("SELECT e FROM Event e WHERE e.eventDate >= CURRENT_DATE AND e.status = 'UPCOMING' AND e.isDeleted = false ORDER BY e.eventDate ASC")
    Page<Event> findUpcomingEvents(Pageable pageable);

    // Find events with available slots
    @Query("SELECT e FROM Event e WHERE e.currentParticipants < e.maxParticipants AND e.registrationDeadline > CURRENT_TIMESTAMP AND e.isDeleted = false")
    Page<Event> findEventsWithAvailableSlots(Pageable pageable);

    List<Event> findByEventDate(LocalDate eventDate);
}