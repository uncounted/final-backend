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
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalItemService {
    private final ItemRepository itemRepository;
    private final SavedItemRepository savedItemRepository;
    private final UserRepository userRepository;
    private final GoalItemRepositroy goalItemRepositroy;

    @Transactional
    public Message postGoalItem(GoalItem.Request goalItemRequest){
        //추후 삭제
        User user = userRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        Long categoryId = goalItemRequest.getCategoryId();
        Long itemId = goalItemRequest.getItemId();
        Item item = itemRepository.findByCategoryIdAndId(categoryId, itemId);
        int count = goalItemRequest.getGoalItemCount();
        int price = goalItemRequest.getPrice();

        int total = (price == 0)? item.getDefaultPrice() * count : goalItemRequest.getPrice() * count;
        double goalPercent = 1.0;

        GoalItem goalItem = goalItemRepositroy.save(new GoalItem(user, count, total, item));

        int savedItemTotal = 0;
        List<SavedItem> savedItemList = savedItemRepository.findAll();
        if(!savedItemList.isEmpty()){
            for(SavedItem savedItem : savedItemList){
                savedItem.setGoalItem(goalItem);
                savedItemTotal += savedItem.getPrice();
                if(savedItemTotal > total) {
                    goalItem.setGoalPercent(100.0);
                    goalItem.setCheckReached(true);
                    break;
                }
            }
            goalPercent = (double) savedItemTotal / total * 100 ;
        }
        goalItem.setGoalPercent((double)Math.round(goalPercent*100)/100);

        return new Message(true, "목표 항목을 등록하였습니다.", goalItem);
    }



}

