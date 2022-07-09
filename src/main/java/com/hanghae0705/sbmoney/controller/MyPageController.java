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
    ResponseEntity<Message> getProfile() {
        return ResponseEntity.ok(myProfileService.getProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<Message> updateProfile(@RequestPart User.RequestProfile requestProfile,
                                                 @RequestPart(value = "image", required = false) MultipartFile profileImg) throws IOException {
    return ResponseEntity.ok(myProfileService.updateProfile(requestProfile, profileImg));
    }

    @GetMapping("/favorite")
    public ResponseEntity<Message> getFavorite() {
        return ResponseEntity.ok(favoriteService.getFavorite());
    }

    @PostMapping("/favorite")
    public ResponseEntity<Message> createFavorite(@RequestBody Item.Request request) {
        return ResponseEntity.ok(favoriteService.createFavorite(request));
    }

    @PutMapping("/favorite/{favoriteItemId}")
    public ResponseEntity<Message> updateFavorite(@PathVariable Long favoriteItemId, @RequestBody Favorite.UpdateFavorite request) {
        return ResponseEntity.ok(favoriteService.updateFavorite(favoriteItemId, request));
    }

    @PostMapping("/favorite/{favoriteItemId}")
    public ResponseEntity<Message> addFavorite(@PathVariable Long favoriteItemId, @RequestBody Favorite.Request request) {
        Message message = favoriteService.addFavorite(favoriteItemId, request);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/favorite/{favoriteItemId}")
    public ResponseEntity<Message> deleteFavorite(@PathVariable Long favoriteItemId) {
        return ResponseEntity.ok(favoriteService.deleteFavorite(favoriteItemId));
    }
}