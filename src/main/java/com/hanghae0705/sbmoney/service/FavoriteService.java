package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.data.Message;
import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRequestException;
import com.hanghae0705.sbmoney.model.domain.Category;
import com.hanghae0705.sbmoney.model.domain.Favorite;
import com.hanghae0705.sbmoney.model.domain.Item;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.CategoryRepository;
import com.hanghae0705.sbmoney.repository.FavoriteRepository;
import com.hanghae0705.sbmoney.repository.ItemRepository;
import com.hanghae0705.sbmoney.repository.UserRepository;
import com.hanghae0705.sbmoney.security.SecurityUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, @Lazy ItemRepository itemRepository, UserRepository userRepository, @Lazy CategoryRepository categoryRepository) {
        this.favoriteRepository = favoriteRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }



    public Message createFavorite(Item.Request request) {
        System.out.println(request.getCategoryId() + " 서비스 카테고리 아이디");
        if(checkValueIsEmptyByRepo("category", request.getCategoryId())) {
            Item item = new Item(request, categoryRepository.findById(request.getCategoryId()).orElseThrow(
                    () -> new IllegalArgumentException("으잉?")
            ));
            Favorite favorite = new Favorite(request, getUser(), item);
            itemRepository.save(item);
            favoriteRepository.save(favorite);
        } else {
            throw new NullPointerException("존재하지 않는 카테고리");
        }
        return new Message(true, "추가에 성공했습니다");
    }


//    public Message editFaverite() {
//
//    }
//
//    public Message deleteFavorite() {
//
//    }

    public boolean checkValueIsEmptyByRepo(String repoName, Long id) {
        switch(repoName) {
            case "category":
                return categoryRepository.findById(id).isPresent();
            case "favorite":
                return favoriteRepository.findById(id).isPresent();
            case "item":
                return itemRepository.findById(id).isPresent();
        }
        return false;
    }

    public User getUser(){
        User currentUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(
                () -> new ApiRequestException(ApiException.NOT_EXIST_USER)
        );
        return currentUser;
    }

}
