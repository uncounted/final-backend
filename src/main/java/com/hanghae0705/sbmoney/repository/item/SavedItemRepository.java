package com.hanghae0705.sbmoney.repository.item;

import com.hanghae0705.sbmoney.model.domain.item.GoalItem;
import com.hanghae0705.sbmoney.model.domain.item.SavedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SavedItemRepository extends JpaRepository<SavedItem, Long> {

    List<SavedItem> findAllByGoalItemAndAndCreatedAtIsBefore(GoalItem goalItem, LocalDateTime createdDate);
}
