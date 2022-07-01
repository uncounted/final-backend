package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.GoalItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalItemRepositroy extends JpaRepository<GoalItem, Long> {
    GoalItem findAllByOrderByCreatedAtDesc(Long goalId, Boolean checkReached);

}
