package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.domain.GoalItem;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.service.CommonService;
import com.hanghae0705.sbmoney.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommonService commonService;

    @PostMapping("/api/newSavedItem")
    public ResponseEntity<Message> postNewSavedItem(@RequestBody Item.savedItemRequest itemRequest) throws ItemException {
        //바로 티끌에 추가
        User user = commonService.getUser();
        Message message = itemService.postNewSavedItem(itemRequest, user);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/api/newGoalItem")
    public ResponseEntity<Message> postNewGoalItem(@RequestPart(value = "image",required = false) MultipartFile multipartFile,
                                                   @RequestPart(value = "goalItem") Item.goalItemRequest itemRequest) throws ItemException, IOException {
        //바로 티끌에 추가
        User user = commonService.getUser();
        Message message = itemService.postNewGoalItem(itemRequest, multipartFile, user);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/api/item")
    public ResponseEntity<Message> getItems(){
        Message message = itemService.getItems();
        return ResponseEntity.ok(message);
    }
}
