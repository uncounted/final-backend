package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.model.domain.Favorite;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.service.FavoriteService;
import com.hanghae0705.sbmoney.service.MyProfileService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOError;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/mypage")
public class MyPageController {
    private final MyProfileService myProfileService;
    private final FavoriteService favoriteService;

    public MyPageController(MyProfileService myProfileService, FavoriteService favoriteService) {
        this.myProfileService = myProfileService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/profile")
    public ResponseEntity<Message> getProfile() {
        Message message = myProfileService.getProfile();
        return ResponseEntity.ok(message);
    }

    @PutMapping("/profile")
    public ResponseEntity<Message> updateProfile(@RequestPart User.RequestProfile changeInfo,
                                                 @RequestPart(value = "image", required = false) MultipartFile profileImg) throws IOException {
        Message message = myProfileService.updateProfile(changeInfo, profileImg);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/favorite")
    public ResponseEntity<Message> getFavorite() {
        Message message = favoriteService.getFavorite();
        return ResponseEntity.ok(message);
    }

//    @PostMapping("/favorite")
//    public ResponseEntity<Message> createFavorite(@RequestBody Item.Request request) {
//        Message message = favoriteService.createFavorite(request);
//        return ResponseEntity.ok(message);
//    }

    @PutMapping("/favorite/{favoriteItemId}")
    public ResponseEntity<Message> updateFavorite(@PathVariable Long favoriteItemId, @RequestBody Favorite.UpdateFavorite request) {
        Message message = favoriteService.updateFavorite(favoriteItemId, request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/favorite")
    public ResponseEntity<Message> addFavorite(@RequestBody Favorite.Request request) {
        Message message = favoriteService.addFavorite(request);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/favorite/{favoriteItemId}")
    public ResponseEntity<Message> deleteFavorite(@PathVariable Long favoriteItemId) {
        Message message = favoriteService.deleteFavorite(favoriteItemId);
        return ResponseEntity.ok(message);
    }
}