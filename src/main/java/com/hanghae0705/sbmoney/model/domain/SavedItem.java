package com.hanghae0705.sbmoney.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hanghae0705.sbmoney.model.domain.baseEntity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@RequiredArgsConstructor
public class SavedItem extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonBackReference(value = "user-fk")
    private User user;

    @Column(nullable = false)
    private int price;

    @OneToOne
    @JoinColumn(name = "ITEM_ID")
    @JsonBackReference(value = "item-fk")
    private Item item;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "GOAL_ITEM_ID")
    @JsonBackReference(value = "goalItem-fk")
    private GoalItem goalItem;

    public SavedItem(Item item, int price, User user, GoalItem goalItem){
        this.item = item;
        this.price = price;
        this.user = user;
        this.goalItem = goalItem;
    }


    public void update(int price){
        this.price = price;
    }

    public void setGoalItem(GoalItem goalItem){
        this.goalItem = goalItem;
    }


    @Getter
    public static class Request {
        private Long itemId;
        private Long goalItemId;
        private int price;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class getRequest {
        private Long goalItemId;
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        private int price;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long categoryId;
        private String categoryName;
        private Long itemId;
        private String itemName;
        private int price;
    }

}
