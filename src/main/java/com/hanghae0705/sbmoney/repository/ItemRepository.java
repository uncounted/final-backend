package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByCategoryIdAndId(Long categoryId, Long itemId);
}
