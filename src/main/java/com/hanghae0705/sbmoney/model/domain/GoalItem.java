package com.hanghae0705.sbmoney.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghae0705.sbmoney.model.domain.baseEntity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class GoalItem extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonBackReference(value = "user-fk")
    User user;

    @Column
    LocalDateTime reachedAt;

    @Column
    String image;

    @Column(nullable = false)
    int count;

    @Column(nullable = false)
    int total;

    @OneToOne
    @JoinColumn(name = "ITEM_ID")
    @JsonBackReference(value = "item-fk")
    Item item;

    @OneToMany(mappedBy = "goalItem")
    @JsonManagedReference(value = "goalItem-fk")
    private List<SavedItem> savedItems;

    @Column
    boolean checkReached;

    @Column
    double goalPercent;

    public GoalItem(User user, int count, int total, Item item) {
        this.user = user;
        this.count = count;
        this.total = total;
        this.item = item;
    }

    public void setGoalPercent(double goalPercent){
        this.goalPercent = goalPercent;
    }

    public void setCheckReached(boolean checkReached){
        this.checkReached = checkReached;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private Long categoryId;
        private Long itemId;
        private int goalItemCount;
        private int price;
    }


}
