package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.dto.Message;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.SavedItem;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.SavedItemRepository;
import com.hanghae0705.sbmoney.repository.UserRepository;
import com.hanghae0705.sbmoney.util.MathFloor;
import com.hanghae0705.sbmoney.validator.ItemValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedItemService {
    private final SavedItemRepository savedItemRepository;
    private final UserRepository userRepository;
    private final ItemValidator itemValidator;

    @Transactional
    public Message postSavedItem(SavedItem.Request savedItemRequest) throws ItemException {
        //추후 Authentication 으로 유저 정보 받아오기
        User user = userRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다")
        );
        Item item = itemValidator.isValidItem(savedItemRequest.getItemId());

        GoalItem noGoalItem = itemValidator.isValidGoalItem(-1L);

        int price = (savedItemRequest.getPrice() == 0) ? item.getDefaultPrice() : savedItemRequest.getPrice();

        //GoalItem이 등록되지 않았을 때
        if (savedItemRequest.getGoalItemId() == -1) {
            savedItemRepository.save(new SavedItem(item, price, user, noGoalItem));
        } else { //GoalItem이 등록되었을 때
            GoalItem goalItem = itemValidator.isValidGoalItem(savedItemRequest.getGoalItemId());
            itemValidator.isReachedGoalItem(goalItem.getGoalPercent());
            int savedItemTotal = 0;

            for (SavedItem savedItem : goalItem.getSavedItems()) {
                savedItemTotal += savedItem.getPrice();
            }
            int updatePrice = savedItemTotal + price;


            if (updatePrice > goalItem.getTotal()) { // GoalItem이 목표 금액을 달성했을 때
                LocalDateTime reachedAt = LocalDateTime.now();
                goalItem.setCheckReached(true, 100.0, reachedAt);
                savedItemRepository.save(new SavedItem(item, price, user, goalItem));
            } else { // GoalItem이 목표 금액을 달성하지 못했을 때
                double decimal = ((double) updatePrice / goalItem.getTotal());
                double updateGoalPercent = MathFloor.PercentTenths(decimal);
                savedItemRepository.save(new SavedItem(item, price, user, goalItem));
                goalItem.setGoalPercent(updateGoalPercent);
            }
        }

        return new Message(true, "티끌 등록에 성공했습니다.");
    }

    public Message getSavedItems() {
        List<SavedItem> savedItemList = savedItemRepository.findAll();
        List<SavedItem.Response> savedItemResponseList = new ArrayList<>();
        for (SavedItem savedItem : savedItemList) {
            Long categoryId = savedItem.getItem().getCategory().getId();
            String categoryName = savedItem.getItem().getCategory().getName();
            Long itemId = savedItem.getItem().getId();
            String itemName = savedItem.getItem().getName();
            int price = savedItem.getPrice();

            SavedItem.Response savedItemResponse = new SavedItem.Response(categoryId, categoryName, itemId, itemName, price);
            savedItemResponseList.add(savedItemResponse);
        }
        return new Message(true, "티끌 조회에 성공했습니다.", savedItemResponseList);
    }

    @Transactional
    public Message updateSavedItem(Long itemId, SavedItem.Update price) {
        SavedItem savedItem = savedItemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 티끌입니다.")
        );
        savedItem.update(price.getPrice());
        return new Message(true, "티끌 수정에 성공했습니다.");
    }
}
