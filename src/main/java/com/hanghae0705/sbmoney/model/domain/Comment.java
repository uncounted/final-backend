package com.hanghae0705.sbmoney.model.domain;

import com.hanghae0705.sbmoney.model.domain.baseEntity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    User user;

    @Column(nullable = false, length = 200)
    private String comment;

    @ManyToOne
    @JoinColumn(name="BOARD_ID")
    Board board;

    @Getter
    @AllArgsConstructor
    public static class Request {

        private String comment;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {

        private String comment;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public Response(Comment comment){
            this.comment = comment.getComment();
            this.createdAt = comment.getCreatedDate();
            this.modifiedAt = comment.getModifiedDate();
        }
    }

    public Comment(Request request){
        this.comment = request.getComment();
    }




    // 댓글 수정
    public void updateComment(Request request) {

        this.comment = request.getComment();

    }



}