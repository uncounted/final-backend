package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.service.GoalItemService;
import com.hanghae0705.sbmoney.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GoalItemController {
    private final GoalItemService goalItemService;

    @PostMapping("/api/goalItem/{goalItemId}")
    public ResponseEntity<Message> uploadImage(@PathVariable Long goalItemId, @RequestPart(value = "file") MultipartFile multipartFile) throws IOException, ItemException {
        Message message = goalItemService.uploadImage(goalItemId, multipartFile);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/api/goalItem")
    public ResponseEntity<Message> getGoalItemList() {
        Message message = goalItemService.getGoalItemList();
        return ResponseEntity.ok(message);
    }

    @GetMapping("/api/mypage/history")
    public ResponseEntity<Message> getHistory() {
        Message message = goalItemService.getHistory();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/api/goalItem")
    public ResponseEntity<Message> postGoalItem(@RequestBody GoalItem.Request goalItemRequest) throws ItemException {
        //JWT, 스프링 시큐리티 병합 후 활성화
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.geyPrincipal()
//        User user = userDetails.getUser();

        Message message = goalItemService.postGoalItem(goalItemRequest);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/api/goalItem/{goalItemId}")
    public ResponseEntity<Message> updateGoalItem(@PathVariable Long goalItemId, @RequestBody GoalItem.Request goalItemRequest) throws ItemException {
        Message message = goalItemService.updateGoalItem(goalItemId, goalItemRequest);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/api/goalItem/{goalItemId}")
    public ResponseEntity<Message> deleteGoalItem(@PathVariable Long goalItemId) {
        Message message = goalItemService.deleteGoalItem(goalItemId);
        return ResponseEntity.ok(message);
    }
}