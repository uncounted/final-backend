package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.Favorite;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.service.FavoriteService;
import com.hanghae0705.sbmoney.service.MyProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/mypage")
public class MyPageController {

    private final MyProfileService myProfileService;

    private final FavoriteService favoriteService;

    public MyPageController(MyProfileService myProfileService, FavoriteService favoriteService) {
        this.myProfileService = myProfileService;
        this.favoriteService = favoriteService;
    }


    @PutMapping("/profile")
    public ResponseEntity<Message> updateProfile(@RequestBody User.RequestProfile requestProfile){
        Message message = myProfileService.updateProfile(requestProfile);
        return ResponseEntity.ok(message);
    }
//
//    @GetMapping("/favorite")
//    public ResponseEntity<Message> getFavorite(){
//        return;
//    }
//
    @PostMapping("/favorite")
    public ResponseEntity<Message> createFavorite(@RequestBody Item.Request request){
        System.out.println(request.getItemName() + " 컨트롤러 아이템 네임");
        System.out.println(request.getCategoryId() + " 컨트롤러 카테고리 id");
        Message message = favoriteService.createFavorite(request);
        return ResponseEntity.ok(message);
    }

//    @PostMapping("/favorite/{favoriteItemId}")
//    public ResponseEntity<Message> addFavorite(@PathVariable Long favoriteItemId, @RequestBody Favorite.Request request){
//        Message message = new Message();
//        return ResponseEntity.ok(message);
//    }

//    @PutMapping("/favorite/{favoriteItemId}")
//    public ResponseEntity<Message> editFavorite(@PathVariable Long favoriteItemId) {
//
//    }
//
//    @DeleteMapping("/favorite/{favoriteItemId}")
//    public ResponseEntity<Message> deleteFavorite(@PathVariable Long favoriteItemId) {
//
//    }

}