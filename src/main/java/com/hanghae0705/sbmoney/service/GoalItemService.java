package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.dto.Message;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.SavedItem;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.GoalItemRepository;
import com.hanghae0705.sbmoney.repository.SavedItemRepository;
import com.hanghae0705.sbmoney.repository.UserRepository;
import com.hanghae0705.sbmoney.util.MathFloor;
import com.hanghae0705.sbmoney.validator.ItemValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalItemService {
    private final SavedItemRepository savedItemRepository;
    private final UserRepository userRepository;
    private final GoalItemRepository goalItemRepository;
    private final S3Uploader s3Uploader;
    private final ItemValidator itemValidator;


    public Message getGoalItemList() {
        GoalItem goalItem = goalItemRepository.findByCheckReached(false);
        GoalItem.Response goalItemResponse = new GoalItem.Response(goalItem);
        return new Message(true, "목표 항목을 조회하였습니다.", goalItemResponse);
    }

    public Message getHistory(){
        List<GoalItem> goalItemList = goalItemRepository.findAllByCheckReachedOrderByCreatedAtDesc(true);
        return new Message(true, "히스토리를 성공적으로 조회하였습니다.", goalItemList);
    }

    @Transactional
    public Message uploadImage(Long goalItemId, MultipartFile multipartFile) throws IOException, ItemException {
        GoalItem goalItem = itemValidator.isValidGoalItem(goalItemId);
        String url = s3Uploader.upload(multipartFile, "static");

        goalItem.setImage(url);
        return new Message(true, "이미지를 등록하였습니다.", url);
    }

    @Transactional
    public Message postGoalItem(GoalItem.Request goalItemRequest) throws ItemException {
        //추후 삭제
        User user = userRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
        Long categoryId = goalItemRequest.getCategoryId();
        Long itemId = goalItemRequest.getItemId();
        Item item = itemValidator.isValidCategoryAndItem(categoryId, itemId);

        int count = goalItemRequest.getGoalItemCount();
        int price = goalItemRequest.getPrice();

        int total = (price == 0) ? item.getDefaultPrice() * count : goalItemRequest.getPrice() * count;

        GoalItem goalItem = goalItemRepository.save(new GoalItem(user, count, total, item));

        List<SavedItem> savedItemList = savedItemRepository.findAll();

        return new Message(true, "목표 항목을 등록하였습니다.", goalItem);
    }

    @Transactional
    public Message updateGoalItem(Long goalItemId, GoalItem.Request goalItemRequest) throws ItemException {
        GoalItem goalItem = itemValidator.isValidGoalItem(goalItemId);

        // 목표 품목을 변경할 때
        if (goalItemRequest.getItemId() != -1) {
            Long categoryId = goalItemRequest.getCategoryId();
            Long itemId = goalItemRequest.getItemId();
            Item item = itemValidator.isValidCategoryAndItem(categoryId, itemId);
            int count = goalItemRequest.getGoalItemCount();
            int price = goalItemRequest.getPrice();
            int total = (price == 0) ? item.getDefaultPrice() * count : goalItemRequest.getPrice() * count;
            int savedItemTotal = 0;
            double goalPercent = 1.0;

            List<SavedItem> savedItems = goalItem.getSavedItems();

            for (SavedItem savedItem : savedItems) {
                savedItemTotal += savedItem.getPrice();
            }

            double decimal = (double) savedItemTotal / total;
            goalPercent = MathFloor.PercentTenths(decimal);

            if (savedItemTotal >= total) { // 변경한 품목이 달성률 100%를 넘은 지점
                LocalDateTime reachedAt = LocalDateTime.now();
                goalItem.setCheckReached(true, reachedAt);
            }
            goalItem.updateGoalItem(count, total, item, goalPercent);
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
                savedItemTotal += savedItem.getPrice();
                if (savedItemTotal >= total) { // 수량/금액 변경으로 달성률 100%를 넘은 지점
                    LocalDateTime reachedAt = LocalDateTime.now();
                    goalItem.setCheckReached(true, reachedAt);
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
            if (savedItem.getGoalItem().getId() != -1 && savedItem.getGoalItem().getId().equals(goalItemId)) {
                savedItem.setGoalItem(null);
            }
        }
        goalItemRepository.deleteById(goalItemId);
        return new Message(true, "목표 항목을 삭제하였습니다.");
    }


}

