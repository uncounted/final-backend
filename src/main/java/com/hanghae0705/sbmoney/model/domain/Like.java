package com.hanghae0705.sbmoney.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@RequiredArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LIKE_ID")
    private Long id;

    @NotNull
    private boolean like;

//    @ManyToOne
//    @JoinColumn(name="USER_ID")
//    private User user;

    @ManyToOne
    @JoinColumn(name="BOARD_ID")
    private Board board;

    public Like(Board board){
        this.like = true;
//        this.user =
        this.board = board;
    }
    public void changeLike(boolean like){
        this.like = like;
    }


    @Getter
    public static class Response{
        private Long likeCount;
        private boolean checkLike;

        public Response(Long likeCount, boolean checkLike ){
            this.checkLike = checkLike;
            this.likeCount = likeCount;
        }
    }
}
