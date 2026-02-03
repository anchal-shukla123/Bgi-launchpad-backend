// package com.bgi.launchpad.controller;
// import com.bgi.launchpad.model.Announcement;
// import com.bgi.launchpad.service.AnnouncementService;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import java.util.List;

// @RestController
// @RequestMapping("/api/announcements")
// public class AnnouncementController {
//   private final AnnouncementService announcementService;

//     public AnnouncementController(AnnouncementService announcementService) {
//         this.announcementService = announcementService;
//     }

//     // CREATE ANNOUNCEMENT
//     @PostMapping
//     public ResponseEntity<Announcement> create(@RequestBody Announcement announcement) {
//         return ResponseEntity.ok(
//                 announcementService.createAnnouncement(announcement)
//         );
//     }

//     // GET ALL ANNOUNCEMENTS
//     @GetMapping
//     public ResponseEntity<List<Announcement>> getAll() {
//         return ResponseEntity.ok(
//                 announcementService.getAllAnnouncements()
//         );
//     }

//     // GET ANNOUNCEMENTS BY DEPARTMENT
//     @GetMapping("/department/{departmentId}")
//     public ResponseEntity<List<Announcement>> getByDepartment(
//             @PathVariable Long departmentId) {

//         return ResponseEntity.ok(
//                 announcementService.getByDepartment(departmentId)
//         );
//     }
// }


package com.bgi.launchpad.controller;

import com.bgi.launchpad.model.Announcement;
import com.bgi.launchpad.service.AnnouncementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "*") // Add this if you have CORS issues
public class AnnouncementController {
    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    // CREATE ANNOUNCEMENT
    @PostMapping
    public ResponseEntity<Announcement> create(@RequestBody Announcement announcement) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(announcementService.createAnnouncement(announcement));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL ANNOUNCEMENTS
    @GetMapping
    public ResponseEntity<List<Announcement>> getAll() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    // GET ANNOUNCEMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getById(@PathVariable Long id) {
        return announcementService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET ANNOUNCEMENTS BY DEPARTMENT
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Announcement>> getByDepartment(
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(announcementService.getByDepartment(departmentId));
    }

    // UPDATE ANNOUNCEMENT
    @PutMapping("/{id}")
    public ResponseEntity<Announcement> update(
            @PathVariable Long id,
            @RequestBody Announcement announcement) {
        try {
            Announcement updated = announcementService.updateAnnouncement(id, announcement);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE ANNOUNCEMENT (Soft Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        try {
            announcementService.deleteAnnouncement(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Announcement deleted successfully",
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
                    "message", "Failed to delete announcement: " + e.getMessage()
                ));
        }
    }

    // HARD DELETE ANNOUNCEMENT (Permanently removes from database)
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> permanentDelete(@PathVariable Long id) {
        try {
            announcementService.permanentDeleteAnnouncement(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Announcement permanently deleted",
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

    // INCREMENT VIEW COUNT
    @PostMapping("/{id}/view")
    public ResponseEntity<Map<String, Object>> incrementView(@PathVariable Long id) {
        try {
            announcementService.incrementViewCount(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "View count incremented"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "success", false,
                    "message", e.getMessage()
                ));
        }
    }

    // INCREMENT COMMENT COUNT
    @PostMapping("/{id}/comment")
    public ResponseEntity<Map<String, Object>> incrementComment(@PathVariable Long id) {
        try {
            announcementService.incrementCommentCount(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Comment count incremented"
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