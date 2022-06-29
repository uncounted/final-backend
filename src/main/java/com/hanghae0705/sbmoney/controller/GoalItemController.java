package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.model.dto.Message;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.service.GoalItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GoalItemController {
    private final GoalItemService goalItemService;

    @GetMapping("/api/goalItem")
    public ResponseEntity<Message> getGoalItemList(){
        Message message = goalItemService.getGoalItemList();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/api/goalItem")
    public ResponseEntity<Message> postGoalItem(@RequestBody GoalItem.Request goalItemRequest){
        //JWT, 스프링 시큐리티 병합 후 활성화
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.geyPrincipal()
//        User user = userDetails.getUser();
        Message message = goalItemService.postGoalItem(goalItemRequest);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/api/goalItem/{goalItemId}")
    public ResponseEntity<Message> updateGoalItem(@PathVariable Long goalItemId, @RequestBody GoalItem.Request goalItemRequest){
        Message message = goalItemService.updateGoalItem(goalItemId, goalItemRequest);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/api/goalItem/{goalItemId}")
    public ResponseEntity<Message> deleteGoalItem(@PathVariable Long goalItemId){
        Message message = goalItemService.deleteGoalItem(goalItemId);
        return ResponseEntity.ok(message);
    }
}
