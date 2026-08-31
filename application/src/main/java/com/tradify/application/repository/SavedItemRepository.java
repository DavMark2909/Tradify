package com.tradify.application.repository;

import com.tradify.application.entity.SavedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedItemRepository extends JpaRepository<SavedItem, Long> {

    Page<SavedItem> findByBuyerId(Long userId, Pageable pageable);
    long deleteByIdAndBuyer_Id(Long id, Long buyerId);
}
