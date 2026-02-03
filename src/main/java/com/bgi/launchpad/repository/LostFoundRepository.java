package com.bgi.launchpad.repository;

import com.bgi.launchpad.model.LostFound;
import com.bgi.launchpad.model.enums.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LostFoundRepository extends JpaRepository<LostFound, Long> {

    // Find all non-deleted items
    Page<LostFound> findAllByIsDeletedFalse(Pageable pageable);

    // Find by status
    Page<LostFound> findByStatusAndIsDeletedFalse(ItemStatus status, Pageable pageable);

    // Find by category
    Page<LostFound> findByCategoryAndIsDeletedFalse(String category, Pageable pageable);

    // Search by keyword
    @Query("SELECT l FROM LostFound l WHERE l.isDeleted = false AND (LOWER(l.itemName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<LostFound> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Find expired items
    @Query("SELECT l FROM LostFound l WHERE l.expiresAt < CURRENT_TIMESTAMP AND l.status != 'CLAIMED' AND l.isDeleted = false")
    Page<LostFound> findExpiredItems(Pageable pageable);

    // Find by user
    Page<LostFound> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);
}