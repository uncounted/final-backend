package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.model.dto.Message;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.SavedItem;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.GoalItemRepositroy;
import com.hanghae0705.sbmoney.repository.ItemRepository;
import com.hanghae0705.sbmoney.repository.SavedItemRepository;
import com.hanghae0705.sbmoney.repository.UserRepository;
import com.hanghae0705.sbmoney.util.MathFloor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalItemService {
    private final ItemRepository itemRepository;
    private final SavedItemRepository savedItemRepository;
    private final UserRepository userRepository;
    private final GoalItemRepositroy goalItemRepositroy;

    @Transactional
    public Message getGoalItemList() {
        List<GoalItem> goalItemList = goalItemRepositroy.findAll();
        List<GoalItem.Response> goalItemResponseList = new ArrayList<>();
        for (GoalItem goalItem : goalItemList) {
            GoalItem.Response goalItemResponse = new GoalItem.Response(goalItem);
            goalItemResponseList.add(goalItemResponse);
        }
        return new Message(true, "목표 항목을 조회하였습니다.", goalItemResponseList);
    }

    @Transactional
    public Message postGoalItem(GoalItem.Request goalItemRequest) {
        //추후 삭제
        User user = userRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        Long categoryId = goalItemRequest.getCategoryId();
        Long itemId = goalItemRequest.getItemId();
        Item item = itemRepository.findByCategoryIdAndId(categoryId, itemId);
        int count = goalItemRequest.getGoalItemCount();
        int price = goalItemRequest.getPrice();

        int total = (price == 0) ? item.getDefaultPrice() * count : goalItemRequest.getPrice() * count;

        GoalItem goalItem = goalItemRepositroy.save(new GoalItem(user, count, total, item));

        List<SavedItem> savedItemList = savedItemRepository.findAll();
        int savedItemTotal = 0;
        if (!savedItemList.isEmpty()) {
            for (SavedItem savedItem : savedItemList) {
                if (savedItem.getGoalItem() == null) {
                    savedItem.setGoalItem(goalItem); //savedItem의 goalItem이 null인 것들에게 goalItem 지정

                    savedItemTotal += savedItem.getPrice();
                    if (savedItemTotal >= total) { // 수량/금액 변경으로 달성률 100%를 넘은 지점
                        LocalDateTime reachedAt = LocalDateTime.now();
                        goalItem.setCheckReached(true, 100.0, reachedAt);
                        return new Message(true, "목표 등록이 완료되었습니다.");
                    }
                }
            }
            double decimal = (double) savedItemTotal / total;
            double goalPercent = MathFloor.PercentTenths(decimal);
            goalItem.updateGoalItem(count, total, goalPercent);

            return new Message(true, "목표 등룍이 완료되었습니다.");
        }

        return new Message(true, "목표 항목을 등록하였습니다.", goalItem);
    }

    @Transactional
    public Message updateGoalItem(Long goalItemId, GoalItem.Request goalItemRequest) {
        GoalItem goalItem = goalItemRepositroy.findById(goalItemId).orElseThrow(
                () -> new IllegalArgumentException("목표가 존재하지 않습니다.")
        );

        // 목표 품목을 변경할 때
        if (goalItemRequest.getItemId() != null) {
            Long categoryId = goalItemRequest.getCategoryId();
            Long itemId = goalItemRequest.getItemId();
            Item item = itemRepository.findByCategoryIdAndId(categoryId, itemId);
            int count = goalItemRequest.getGoalItemCount();
            int price = goalItemRequest.getPrice();
            int total = (price == 0) ? item.getDefaultPrice() * count : goalItemRequest.getPrice() * count;
            int savedItemTotal = 0;
            double goalPercent = 1.0;

            List<SavedItem> savedItems = goalItem.getSavedItems();
            for (SavedItem savedItem : savedItems) {
                if (savedItemTotal >= total) { // 변경한 품목이 달성률 100%를 넘을 때(정렬확인)
                    savedItem.setGoalItem(null);
                    continue;
                }
                savedItemTotal += savedItem.getPrice();
                if (savedItemTotal >= total) { // 변경한 품목이 달성률 100%를 넘은 지점
                    LocalDateTime reachedAt = LocalDateTime.now();
                    goalItem.setCheckReached(true, 100.0, reachedAt);
                }
            }
            if (savedItemTotal < total) { // 변경한 품목이 달성률 100% 미만일 때
                double decimal = (double) savedItemTotal / total;
                goalPercent = MathFloor.PercentTenths(decimal);
                goalItem.setGoalPercent(goalPercent);
                goalItem.updateGoalItem(count, total, item, goalPercent);
            }
        }
        // 목표 품목을 변경하지 않을 때
        else {
            int itemDefaultPrice = goalItem.getItem().getDefaultPrice();
            int count = goalItemRequest.getGoalItemCount();
            int price = goalItemRequest.getPrice();
            int total = (price == 0) ? itemDefaultPrice * count : goalItemRequest.getPrice() * count;
            int savedItemTotal = 0;

            List<SavedItem> savedItems = goalItem.getSavedItems();
            for (SavedItem savedItem : savedItems) {
                if (savedItemTotal >= total) { // 수량/금액 변경으로 달성률 100%를 넘을 때(정렬확인)
                    savedItem.setGoalItem(null);
                    continue;
                }
                savedItemTotal += savedItem.getPrice();
                if (savedItemTotal >= total) { // 수량/금액 변경으로 달성률 100%를 넘은 지점
                    LocalDateTime reachedAt = LocalDateTime.now();
                    goalItem.setCheckReached(true, 100.0, reachedAt);
                }
            }
            double decimal = (double) savedItemTotal / total;
            double goalPercent = MathFloor.PercentTenths(decimal);
            goalItem.updateGoalItem(count, total, goalPercent);
        }
        return new Message(true, "목표 항목을 수정하였습니다.");
    }

    @Transactional
    public Message deleteGoalItem(Long goalItemId) {
        List<SavedItem> savedItemList = savedItemRepository.findAll();
        for (SavedItem savedItem : savedItemList) {
            if (savedItem.getGoalItem() != null && savedItem.getGoalItem().getId().equals(goalItemId)) {
                savedItem.setGoalItem(null);
            }
        }
        goalItemRepositroy.deleteById(goalItemId);
        return new Message(true, "목표 항목을 삭제하였습니다.");
    }


}

