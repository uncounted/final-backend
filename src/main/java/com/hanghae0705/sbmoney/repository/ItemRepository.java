package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByCategoryIdAndId(Long categoryId, Long itemId);
    Optional<Item> findByName(String name);
}
