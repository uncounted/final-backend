package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.SavedItem;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.GoalItemRepositroy;
import com.hanghae0705.sbmoney.repository.ItemRepository;
import com.hanghae0705.sbmoney.repository.SavedItemRepository;
import com.hanghae0705.sbmoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedItemService {
    private final ItemRepository itemRepository;
    private final SavedItemRepository savedItemRepository;
    private final UserRepository userRepository;
    private final GoalItemRepositroy goalItemRepositroy;

    @Transactional
    public Message postSavedItem(SavedItem.Request savedItemRequest){
        //추후 Authentication 으로 유저 정보 받아오기
        User user = userRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다")
        );
        Item item = itemRepository.findById(savedItemRequest.getItemId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 물건입니다.")
        );

        int price = (savedItemRequest.getPrice() == 0) ? item.getDefaultPrice() : savedItemRequest.getPrice();
        if(savedItemRequest.getGoalItemId() == null){
            savedItemRepository.save(new SavedItem(item, price, user));
        } else {
            GoalItem goalItem = goalItemRepositroy.findById(savedItemRequest.getGoalItemId()).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않은 목표입니다.")
            );
            int savedItemTotal = 0;
            for(SavedItem savedItem : goalItem.getSavedItems()){
                savedItemTotal += savedItem.getPrice();
            }
            if(savedItemTotal + price > goalItem.getTotal()){
                goalItem.setCheckReached(true);
                savedItemRepository.save(new SavedItem(item, price, user));
            }
            savedItemRepository.save(new SavedItem(item, price, user, goalItem));
        }

        return new Message(true, "아끼기 품목 등록에 성공했습니다.");
    }

    public Message getSavedItems(){
        List<SavedItem> savedItemList = savedItemRepository.findAll();
        List<SavedItem.Response> savedItemResponseList = new ArrayList<>();
        for(SavedItem savedItem : savedItemList){
            Long categoryId = savedItem.getItem().getCategory().getId();
            String categoryName = savedItem.getItem().getCategory().getName();
            Long itemId = savedItem.getItem().getId();
            String itemName = savedItem.getItem().getName();
            int price = savedItem.getPrice();

            SavedItem.Response savedItemResponse = new SavedItem.Response(categoryId, categoryName, itemId, itemName, price);
            savedItemResponseList.add(savedItemResponse);
        }

        return new Message(true, "아끼기 품목 조회에 성공했습니다.", savedItemResponseList);
    }

    @Transactional
    public Message updateSavedItem(Long itemId, SavedItem.Update price){
        SavedItem savedItem = savedItemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아끼기 항목입니다.")
        );
        savedItem.update(price.getPrice());
        return new Message(true, "수정에 성공했습니다.");
    }
}
