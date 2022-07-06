package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ItemException;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.service.CommonService;
import com.hanghae0705.sbmoney.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/api/item")
    public ResponseEntity<Message> postItem(@RequestBody Item.Request itemRequest) throws ItemException {
        //바로 티끌에 추가
        Message message = itemService.postItem(itemRequest);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/api/item")
    public ResponseEntity<Message> getItems(){
        Message message = itemService.getItems();
        return ResponseEntity.ok(message);
    }
}
