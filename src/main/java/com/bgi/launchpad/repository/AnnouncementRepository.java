package com.bgi.launchpad.repository;

import com.bgi.launchpad.model.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    // Find all non-deleted announcements with pagination
    Page<Announcement> findAllByIsDeletedFalse(Pageable pageable);

    // Find by department with pagination (null means all departments)
    @Query("SELECT a FROM Announcement a WHERE (a.departmentId = :departmentId OR a.departmentId IS NULL) AND a.isDeleted = false")
    Page<Announcement> findByDepartmentIdOrAllDepartments(@Param("departmentId") Long departmentId, Pageable pageable);

    // Search by keyword in title or description
    @Query("SELECT a FROM Announcement a WHERE a.isDeleted = false AND (LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Announcement> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Get announcements with most views
    @Query("SELECT a FROM Announcement a WHERE a.isDeleted = false ORDER BY a.viewCount DESC")
    Page<Announcement> findTopByViewCount(Pageable pageable);
   
   List<Announcement> findByDepartmentId(Long departmentId);

    Page<Announcement> findByCreatedAtAfterAndIsDeletedFalse(
        LocalDateTime cutoff,
        Pageable pageable
    );

}