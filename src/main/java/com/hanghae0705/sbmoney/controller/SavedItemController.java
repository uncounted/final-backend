package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.domain.SavedItem;
import com.hanghae0705.sbmoney.repository.SavedItemRepository;
import com.hanghae0705.sbmoney.service.SavedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SavedItemController {
    private final SavedItemService savedItemService;
    private final SavedItemRepository savedItemRepository;

    @GetMapping("/api/savedItem")
    private ResponseEntity<Message> getSavedItem(){
        Message message = savedItemService.getSavedItems();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/api/savedItem")
    private ResponseEntity<Message> postSavedItem(@RequestBody SavedItem.Request savedItemRequest) throws ItemException {
        //JWT, 스프링 시큐리티 병합 후 활성화
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.geyPrincipal()
//        User user = userDetails.getUser();
        Message message = savedItemService.postSavedItem(savedItemRequest);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/api/savedItem/{itemId}")
    private ResponseEntity<Message> updateSavedItem(@PathVariable Long itemId, @RequestBody SavedItem.Update price){
        Message message = savedItemService.updateSavedItem(itemId, price);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/api/savedItem/{itemId}")
    private ResponseEntity<Message> deleteSavedItem(@PathVariable Long itemId){
        savedItemRepository.deleteById(itemId);
        Message message = new Message(true, "티끌 삭제에 성공했습니다.");
        return ResponseEntity.ok(message);
    }
}