package com.hanghae0705.sbmoney.model.domain;

import com.hanghae0705.sbmoney.model.domain.baseEntity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @NotNull
    private String title;

    @NotNull
    private String contents;

    @Column
    private String image;

    @OneToOne
    @JoinColumn(name = "GOAL_ITEM")
    private GoalItem goalItem;

    @NotNull
    private Long commentCount;

    @NotNull
    private Long likeCount;

    @NotNull
    private Long viewCount;

    @NotNull
    private boolean checkLike;

    public Board(Request request, GoalItem goalItem, Optional<User> user) {
        this.goalItem = goalItem;
        this.image = goalItem.getImage();
        this.title = request.title;
        this.contents = request.contents;
        this.user = user.get();
        this.likeCount = (long) 0;
        this.checkLike = false;
        this.commentCount = (long) 0;
        this.viewCount = (long) 0;
    }

    public void updateBoard(Board.Update update) {
        this.contents = update.contents;
    }

    public void likeBoard(boolean like, Long likeCount) {

        this.checkLike = like;
        this.likeCount = likeCount;
    }

    public void commentCount(Long commentCount){
        this.commentCount = commentCount;
    }

    public void viewCount(Long viewCount){
        this.viewCount = viewCount;
    }

    public void changeImage(String image){
        this.image = image;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        private Long goalItemId;
        private String title;
        private String contents;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        private String title;
        private String contents;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long boardId;
        private String UserId;
        private String nickname;
        private String profileImg;
        private String title;
        private String contents;
        private String image;
        private Long categoryId;
        private String categoryName;
        private Long goalItemId;
        private String goalItemName;
        private double goalPercent;
        private Long likeCount;
        private Long viewCount;
        private Long commentCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private boolean checkLike;

        public Response(Board board) {
            this.boardId = board.getId();
            this.UserId = board.user.getUsername();
            this.nickname = board.user.getNickname();
            this.profileImg = board.user.getProfileImg();
            this.title = board.title;
            this.contents = board.contents;
            this.image = board.image;
            this.categoryId = board.goalItem.getItem().getCategory().getId();
            this.categoryName = board.goalItem.getItem().getCategory().getName();
            this.goalItemId = board.goalItem.getId();
            this.goalItemName = board.goalItem.getItem().getName();
            this.goalPercent =board.goalItem.getGoalPercent();
            this.commentCount = board.commentCount;
            this.viewCount = board.viewCount;
            this.likeCount = board.likeCount;
            this.createdAt = board.getCreatedDate();
            this.modifiedAt = board.getModifiedDate();
            this.likeCount = board.likeCount;
            this.checkLike = board.checkLike;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class SaveItemResponse{
        private Long boardId;
        private String userId;
        private int price;
        private int totalPrice;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private int savedItemTotalPrice;
        private List<SavedItem> savedItemList;

        public SaveItemResponse(Board board, int savedItemTotalPrice){
            this.boardId = board.getId();
            this.userId = board.getUser().getUsername();
            this.totalPrice = board.getGoalItem().getTotal();
            this.price = this.totalPrice/board.getGoalItem().getCount();
            this.createdAt = board.getGoalItem().getCreatedDate();
            this.modifiedAt = board.getGoalItem().getModifiedDate();
            this.savedItemList = board.getGoalItem().getSavedItems();
            this.savedItemTotalPrice = savedItemTotalPrice;
        }
    }
}
