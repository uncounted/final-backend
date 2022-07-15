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

import javax.transaction.Transactional;
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

    public Message getFavorite() {
        List<Favorite.Response> responseList = new ArrayList<>();
        List<Favorite> tempArr = favoriteRepository.findByUser_Id(getUser().getId());
        for(Favorite favorite : tempArr) {
            Favorite.Response response = new Favorite.Response(favorite);
            responseList.add(response);
        }
        return new Message(true, "조회에 성공했습니다.", responseList);
    }

    @Transactional
    public Message addFavorite(Favorite.Request request) {
        if(getValueFromRepoById("category", request.getCategoryId()) == null) {
            throw new ApiRequestException(ApiException.NOT_EXIST_DATA);
        }
        // DB에 없는 아이템을 추가할 경우 request.getitemId == -1
        if(request.getItemId() == -1) {
            Item item = new Item(request, (Category) getValueFromRepoById("category", request.getCategoryId()));
            Favorite favorite = new Favorite(request, getUser(), item);
            itemRepository.save(item);
            favoriteRepository.save(favorite);
        } else {
            favoriteRepository.save(new Favorite(request, getUser(), (Item) getValueFromRepoById("item", request.getItemId())));
        }
        return new Message(true, "추가에 성공했습니다.");
    }

    // Method로 찾은 data 사용 시 우선적으로 비교되야할 Username이 뒤로 밀린다..
    @Transactional
    public Message updateFavorite(Long favoriteItemId, Favorite.UpdateFavorite request) {
        Favorite favorite = (Favorite) getValueFromRepoById("favorite", favoriteItemId);
        compareTwoObjectsIsEqual(getUser().getUsername(), favorite.getUser().getUsername());
        compareTwoObjectsIsNotEqual(request.getPrice(), favorite.getPrice());
        favorite.updateFavorite(request);
        return new Message(true, "수정에 성공했습니다");
    }

    @Transactional
    public Message deleteFavorite(Long favoriteItemId) {
        Favorite favorite = (Favorite) getValueFromRepoById("favorite", favoriteItemId);
        compareTwoObjectsIsEqual(getUser().getUsername(), favorite.getUser().getUsername());
        favoriteRepository.deleteById(favorite.getId());
        return new Message(true, "삭제에 성공했습니다");
    }

    public Object getValueFromRepoById(String repoName, Long id) {
        switch (repoName) {
            case "category":
                return categoryRepository.findById(id).orElseThrow(
                        () -> new ApiRequestException(ApiException.NOT_EXIST_DATA));
            case "favorite":
                return favoriteRepository.findById(id).orElseThrow(
                        () -> new ApiRequestException(ApiException.NOT_EXIST_DATA));
            case "item":
                return itemRepository.findById(id).orElseThrow(
                        () -> new ApiRequestException(ApiException.NOT_EXIST_DATA));
        }
        return false;
    }

    public User getUser() {
        return userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(
                () -> new ApiRequestException(ApiException.NOT_EXIST_USER));
    }

    public void compareTwoObjectsIsEqual(Object target1, Object target2) {
        if (!target1.equals(target2)) {
            throw new IllegalArgumentException("두 인자가 일치하지 않습니다.");
        }
    }

    public void compareTwoObjectsIsNotEqual(Object target1, Object target2) {
        if (target1.equals(target2)) {
            throw new IllegalArgumentException("두 인자가 일치합니다.");
        }
    }

}
