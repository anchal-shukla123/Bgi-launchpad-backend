// package com.bgi.launchpad.service;
// import com.bgi.launchpad.model.Announcement;
// import com.bgi.launchpad.repository.AnnouncementRepository;
// import org.springframework.stereotype.Service;
// import java.util.List;
// import java.util.Optional;

// @Service
// public class AnnouncementService {
//   private final AnnouncementRepository announcementRepository;

//     public AnnouncementService(AnnouncementRepository announcementRepository) {
//         this.announcementRepository = announcementRepository;
//     }

//     // Create announcement
//     public Announcement createAnnouncement(Announcement announcement) {
//         return announcementRepository.save(announcement);
//     }

//     // Get all announcements
//     public List<Announcement> getAllAnnouncements() {
//         return announcementRepository.findAll();
//     }

//     // Get announcements by department
//     public List<Announcement> getByDepartment(Long departmentId) {
//         return announcementRepository.findByDepartmentId(departmentId);
//     }

//     // Get announcement by ID
//     public Optional<Announcement> getById(Long id) {
//         return announcementRepository.findById(id);
//     }
// }

package com.bgi.launchpad.service;

import com.bgi.launchpad.model.Announcement;
import com.bgi.launchpad.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    // Create announcement
    @Transactional
    public Announcement createAnnouncement(Announcement announcement) {
        // Set default values if not provided
        if (announcement.getViewCount() == null) {
            announcement.setViewCount(0L);
        }
        if (announcement.getCommentCount() == null) {
            announcement.setCommentCount(0);
        }
        if (announcement.getHasPoll() == null) {
            announcement.setHasPoll(false);
        }
        
        return announcementRepository.save(announcement);
    }

    // Get all announcements (excluding soft-deleted ones)
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll().stream()
                .filter(announcement -> announcement.getIsDeleted() == null || !announcement.getIsDeleted())
                .toList();
    }

    // Get announcements by department (excluding soft-deleted ones)
    public List<Announcement> getByDepartment(Long departmentId) {
        return announcementRepository.findByDepartmentId(departmentId).stream()
                .filter(announcement -> announcement.getIsDeleted() == null || !announcement.getIsDeleted())
                .toList();
    }

    // Get announcement by ID (excluding soft-deleted ones)
    public Optional<Announcement> getById(Long id) {
        return announcementRepository.findById(id)
                .filter(announcement -> announcement.getIsDeleted() == null || !announcement.getIsDeleted());
    }

    // Update announcement
    @Transactional
    public Announcement updateAnnouncement(Long id, Announcement updatedAnnouncement) {
        Announcement announcement = announcementRepository.findById(id)
                .filter(a -> a.getIsDeleted() == null || !a.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        
        // Update fields
        announcement.setTitle(updatedAnnouncement.getTitle());
        announcement.setDescription(updatedAnnouncement.getDescription());
        announcement.setDepartmentId(updatedAnnouncement.getDepartmentId());
        
        if (updatedAnnouncement.getHasPoll() != null) {
            announcement.setHasPoll(updatedAnnouncement.getHasPoll());
        }
        
        return announcementRepository.save(announcement);
    }

    // Soft delete announcement (marks as deleted but keeps in database)
    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        
        // Check if already deleted
        if (announcement.getIsDeleted() != null && announcement.getIsDeleted()) {
            throw new RuntimeException("Announcement is already deleted");
        }
        
        // Soft delete - just mark as deleted
        announcement.setIsDeleted(true);
        announcementRepository.save(announcement);
    }

    // Permanently delete announcement (removes from database)
    @Transactional
    public void permanentDeleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        
        announcementRepository.delete(announcement);
    }

    // Restore soft-deleted announcement
    @Transactional
    public Announcement restoreAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        
        if (announcement.getIsDeleted() == null || !announcement.getIsDeleted()) {
            throw new RuntimeException("Announcement is not deleted");
        }
        
        announcement.setIsDeleted(false);
        return announcementRepository.save(announcement);
    }

    // Increment view count
    @Transactional
    public void incrementViewCount(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .filter(a -> a.getIsDeleted() == null || !a.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        
        announcement.incrementViewCount();
        announcementRepository.save(announcement);
    }

    // Increment comment count
    @Transactional
    public void incrementCommentCount(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .filter(a -> a.getIsDeleted() == null || !a.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        
        announcement.incrementCommentCount();
        announcementRepository.save(announcement);
    }
}
