package com.hanghae0705.sbmoney.model.domain;

import com.hanghae0705.sbmoney.model.domain.baseEntity.BaseEntity;
import com.hanghae0705.sbmoney.service.BoardService;
import com.hanghae0705.sbmoney.service.LikeService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
    private String contents;

//    private String image;


    @OneToOne
    @JoinColumn(name = "GOAL_ITEM")
    private GoalItem goalItem;


    @NotNull
    private Long likeCount;

    @NotNull
    private boolean checkLike;

    public Board(Request request, GoalItem goalItem, Optional<User> user) {
        this.goalItem = goalItem;
        this.contents = request.contents;
        this.user = user.get();
        this.likeCount = (long) 0;
        this.checkLike = false;
    }

    public void updateBoard(Board.Update update) {
        this.contents = update.contents;
    }

    public void likeBoard(boolean like, Long likeCount) {

        this.checkLike = like;
        this.likeCount = likeCount;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        private Long goalItemId;
        private String contents;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        private String contents;


    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long boardId;
        private String UserId;
        private String nickname;
        private String profileImg;
        private String contents;
        private Long categoryId;
        private String categoryName;
        private Long goalItemId;
        private String goalItemName;
        //        private Long likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private Long likeCount;
        private boolean checkLike;

        public Response(Board board) {
            this.boardId = board.getId();
            this.UserId = board.user.getUsername();
            this.nickname = board.user.getNickname();
            this.profileImg = board.user.getProfileImg();
            this.contents = board.contents;
//            this.categoryId = board.goalItem.item.getCategory().getId();
//            this.categoryName = board.goalItem.item.getCategory().getName();
//            this.goalItemId = board.goalItem.id;
//            this.goalItemName = board.goalItem.item.getName();
//            this.likeCount =
            this.createdAt = board.getCreatedDate();
            this.modifiedAt = board.getModifiedDate();
            this.likeCount = board.likeCount;
            this.checkLike = board.checkLike;
        }
    }
}
