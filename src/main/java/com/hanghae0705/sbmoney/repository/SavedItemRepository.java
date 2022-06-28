package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.SavedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedItemRepository extends JpaRepository<SavedItem, Long> {
}
