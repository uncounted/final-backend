package com.hanghae0705.sbmoney.validator;

import com.hanghae0705.sbmoney.exception.Constants;
import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.repository.GoalItemRepository;
import com.hanghae0705.sbmoney.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemValidator {
    private final GoalItemRepository goalItemRepository;
    private final ItemRepository itemRepository;

    public GoalItem isValidGoalItem(Long goalItemId) throws ItemException {
        return goalItemRepository.findById(goalItemId).orElseThrow(
                () -> new ItemException(Constants.ExceptionClass.GOAL_ITEM, HttpStatus.BAD_REQUEST, "존재하지 않는 태산입니다.")
        );
    }

    public void isReachedGoalItem(double goalPercent) throws ItemException {
        if(goalPercent > 100.0){
            throw new ItemException(Constants.ExceptionClass.SAVED_ITEM, HttpStatus.BAD_REQUEST, "달성율을 초과한 태산입니다.");
        }
    }

    public Item isValidCategoryAndItem(Long categoryId, Long itemId) throws ItemException {
        if(itemId == null){
            throw new ItemException(Constants.ExceptionClass.GOAL_ITEM, HttpStatus.BAD_REQUEST,"존재하지 않는 물건입니다.");
        }
        return itemRepository.findByCategoryIdAndId(categoryId, itemId).orElseThrow(
                () -> new ItemException(Constants.ExceptionClass.GOAL_ITEM, HttpStatus.BAD_REQUEST,"존재하지 않는 물건입니다.")
        );
    }

    public Item isValidItem(Long itemId) throws ItemException {
        if(itemId == null){
            throw new ItemException(Constants.ExceptionClass.GOAL_ITEM, HttpStatus.BAD_REQUEST,"존재하지 않는 물건입니다.");
        }
        return itemRepository.findById(itemId).orElseThrow(
                () -> new ItemException(Constants.ExceptionClass.GOAL_ITEM, HttpStatus.BAD_REQUEST,"존재하지 않는 물건입니다.")
        );
    }
}
