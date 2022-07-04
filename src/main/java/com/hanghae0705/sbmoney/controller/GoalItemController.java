package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.security.SecurityUtil;
import com.hanghae0705.sbmoney.service.CommonService;
import com.hanghae0705.sbmoney.service.GoalItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GoalItemController {
    private final GoalItemService goalItemService;
    private final CommonService commonService;

    @GetMapping("/api/goalItem")
    public ResponseEntity<Message> getGoalItem() {
        User user = commonService.getUser();
        Message message = goalItemService.getGoalItem(user);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/api/mypage/history")
    public ResponseEntity<Message> getHistory() {
        User user = commonService.getUser();
        Message message = goalItemService.getHistory(user);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/api/goalItem/{goalItemId}")
    public ResponseEntity<Message> uploadImage(@PathVariable Long goalItemId, @RequestPart(value = "file") MultipartFile multipartFile) throws IOException, ItemException {
        User user = commonService.getUser();
        Message message = goalItemService.uploadImage(goalItemId, multipartFile, user);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/api/goalItem")
    public ResponseEntity<Message> postGoalItem(@RequestBody GoalItem.Request goalItemRequest) throws ItemException {
        User user = commonService.getUser();
        Message message = goalItemService.postGoalItem(goalItemRequest, user);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/api/goalItem/{goalItemId}")
    public ResponseEntity<Message> updateGoalItem(@PathVariable Long goalItemId, @RequestBody GoalItem.Request goalItemRequest) throws ItemException {
        User user = commonService.getUser();
        Message message = goalItemService.updateGoalItem(goalItemId, goalItemRequest, user);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/api/goalItem/{goalItemId}")
    public ResponseEntity<Message> deleteGoalItem(@PathVariable Long goalItemId) {
        User user = commonService.getUser();
        Message message = goalItemService.deleteGoalItem(goalItemId);
        return ResponseEntity.ok(message);
    }
}