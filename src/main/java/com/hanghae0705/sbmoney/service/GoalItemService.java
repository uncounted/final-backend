package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.Constants;
import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.SavedItem;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.GoalItemRepository;
import com.hanghae0705.sbmoney.repository.SavedItemRepository;
import com.hanghae0705.sbmoney.util.MathFloor;
import com.hanghae0705.sbmoney.validator.ItemValidator;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalItemService {
    private final SavedItemRepository savedItemRepository;
    private final GoalItemRepository goalItemRepository;
    private final S3Uploader s3Uploader;
    private final ItemValidator itemValidator;


    public Message getGoalItem(User user) {
        List<GoalItem> goalItemList = user.getGoalItems();
        GoalItem.Response goalItemResponse = null;
        if(goalItemList != null){
            for(GoalItem goalItem : goalItemList){
                if(goalItem.isCheckReached()){
                    goalItemResponse = new GoalItem.Response(goalItem);
                }
            }
        }
        return new Message(true, "목표 항목을 조회하였습니다.", goalItemResponse);
    }

    public Message getHistory(User user){
        List<GoalItem> goalItemList = user.getGoalItems();
        List<GoalItem.Response> reachedGoalItemList = new ArrayList<>();
        for(GoalItem goalItem : goalItemList){
            if(goalItem.isCheckReached()){
                reachedGoalItemList.add(new GoalItem.Response(goalItem));
            }
        }
        return new Message(true, "히스토리를 성공적으로 조회하였습니다.", reachedGoalItemList);
    }

    @Transactional
    public Message uploadImage(Long goalItemId, MultipartFile multipartFile, User user) throws IOException, ItemException {
        GoalItem goalItem = itemValidator.isValidGoalItem(goalItemId, user);
        String url = s3Uploader.upload(multipartFile, "static");
        goalItem.setImage(url);
        return new Message(true, "이미지를 등록하였습니다.", url);
    }

    @Transactional
    public Message postGoalItem(GoalItem.Request goalItemRequest, User user) throws ItemException {
        List<GoalItem> goalItemList = user.getGoalItems();
        if(goalItemList != null){
            for (GoalItem goalItem : goalItemList){
                if(!goalItem.isCheckReached() && goalItem.getItem().getId() != -1){
                    throw new ItemException(Constants.ExceptionClass.GOAL_ITEM, HttpStatus.BAD_REQUEST, "이미 태산으로 등록된 상품이 존재합니다.");
                } else {
                    LocalDateTime reachedDateTime = LocalDateTime.now();
                    goalItem.setCheckReached(true, reachedDateTime);
                }
            }
        }

        Long categoryId = goalItemRequest.getCategoryId();
        Long itemId = goalItemRequest.getItemId();
        Item item = itemValidator.isValidCategoryAndItem(categoryId, itemId);

        int count = goalItemRequest.getGoalItemCount();
        int price = goalItemRequest.getPrice();

        int total = (price == 0) ? item.getDefaultPrice() * count : goalItemRequest.getPrice() * count;

        GoalItem goalItem = goalItemRepository.save(new GoalItem(user, count, total, item));

        return new Message(true, "목표 항목을 등록하였습니다.", goalItem);
    }

    @Transactional
    public Message updateGoalItem(Long goalItemId, GoalItem.Request goalItemRequest, User user) throws ItemException {
        GoalItem goalItem = itemValidator.isValidGoalItem(goalItemId, user);

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