package com.hanghae0705.sbmoney.model.domain;

import com.hanghae0705.sbmoney.model.domain.baseEntity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BOARD_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @NotNull
    private String title;

    private String image;


    // @onetoone
//    private GoalItem goalItem;

    @NotNull
    private Long viewCount;

//    @NotNull
//    private Like like;


}
