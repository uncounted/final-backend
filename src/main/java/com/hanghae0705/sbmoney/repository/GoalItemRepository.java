package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalItemRepository extends JpaRepository<GoalItem, Long> {
    List<GoalItem> findAllByCheckReachedOrderByCreatedAtDesc(Boolean checkReached);
    GoalItem findByCheckReached(Boolean checkedReached);
    GoalItem findAllById(Long goalId);

    GoalItem findByUserAndAndCheckReachedIsFalse(User user);

}