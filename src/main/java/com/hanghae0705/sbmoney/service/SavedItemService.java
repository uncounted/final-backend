package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.Constants;
import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.SavedItem;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.SavedItemRepository;
import com.hanghae0705.sbmoney.util.MathFloor;
import com.hanghae0705.sbmoney.validator.ItemValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedItemService {
    private final SavedItemRepository savedItemRepository;
    private final ItemValidator itemValidator;

    @Transactional
    public Message postSavedItem(SavedItem.Request savedItemRequest, User user) throws ItemException {
        Item item = itemValidator.isValidItem(savedItemRequest.getItemId());

        int price = (savedItemRequest.getPrice() == 0) ? item.getDefaultPrice() : savedItemRequest.getPrice();

        //GoalItem이 등록되었을 때 (itemId가 -1일 경우 태산 없음)
        GoalItem goalItem = itemValidator.isValidGoalItem(savedItemRequest.getGoalItemId(), user);
        itemValidator.isReachedGoalItem(goalItem.getGoalPercent()); //달성율이 100% 이상이면 등록 불가

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
        return new Message(true, "티끌 등록에 성공했습니다.");
    }

    public Message getSavedItems(SavedItem.getRequest getRequest, User user) throws ItemException {
        //Request에서 goalItem을 던져준다.
        Long goalItemId = getRequest.getGoalItemId();
        GoalItem goalItem = itemValidator.isValidGoalItem(goalItemId, user);

        List<SavedItem> savedItemList = goalItem.getSavedItems();
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
    public Message updateSavedItem(Long itemId, SavedItem.Update price, User user) throws ItemException {
        SavedItem savedItem = savedItemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 티끌입니다.")
        );
        if(!user.equals(savedItem.getUser())){
            throw new ItemException(Constants.ExceptionClass.SAVED_ITEM, HttpStatus.BAD_REQUEST, "유저 아이디가 일치하지 않습니다.");
        }
        savedItem.update(price.getPrice());
        return new Message(true, "티끌 수정에 성공했습니다.");
    }
}