package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.SavedItem;
import com.hanghae0705.sbmoney.repository.SavedItemRepository;
import com.hanghae0705.sbmoney.service.SavedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    private ResponseEntity<Message> postSavedItem(@RequestBody SavedItem.Request savedItemRequest){
        //JWT, 스프링 시큐리티 병합 후 활성화
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.geyPrincipal()
//        User user = userDetails.getUser();
        Message message = savedItemService.postSavedItem(savedItemRequest);
        return ResponseEntity.ok(message);
    }
}
