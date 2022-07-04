package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.domain.SavedItem;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.SavedItemRepository;
import com.hanghae0705.sbmoney.service.CommonService;
import com.hanghae0705.sbmoney.service.SavedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SavedItemController {
    private final SavedItemService savedItemService;
    private final SavedItemRepository savedItemRepository;
    private final CommonService commonService;

    @GetMapping("/api/savedItem")
    private ResponseEntity<Message> getSavedItem(@RequestBody Long goalItemId) throws ItemException {
        User user = commonService.getUser();
        Message message = savedItemService.getSavedItems(goalItemId, user);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/api/savedItem")
    private ResponseEntity<Message> postSavedItem(@RequestBody SavedItem.Request savedItemRequest) throws ItemException {
        User user = commonService.getUser();
        Message message = savedItemService.postSavedItem(savedItemRequest, user);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/api/savedItem/{itemId}")
    private ResponseEntity<Message> updateSavedItem(@PathVariable Long itemId, @RequestBody SavedItem.Update price) throws ItemException {
        User user = commonService.getUser();
        Message message = savedItemService.updateSavedItem(itemId, price, user);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/api/savedItem/{itemId}")
    private ResponseEntity<Message> deleteSavedItem(@PathVariable Long itemId){
        savedItemRepository.deleteById(itemId);
        Message message = new Message(true, "티끌 삭제에 성공했습니다.");
        return ResponseEntity.ok(message);
    }
}