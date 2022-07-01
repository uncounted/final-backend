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
    @Column
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


    public void setCheckReached(boolean checkReached, double goalPercent, LocalDateTime reachedAt){
        this.checkReached = checkReached;
        this.goalPercent = goalPercent;
        this.reachedAt = reachedAt;
    }

    public void setCheckReached(boolean checkReached, LocalDateTime reachedAt){
        this.checkReached = checkReached;
        this.reachedAt = reachedAt;
    }

    public void setGoalPercent(double goalPercent){
        this.goalPercent = goalPercent;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void updateGoalItem(int count, int total, double goalPercent){
        this.count = count;
        this.total = total;
        this.goalPercent = goalPercent;
    }

    public void updateGoalItem(int count, int total, Item item, double goalPercent) {
        this.count = count;
        this.total = total;
        this.item = item;
        this.goalPercent = goalPercent;
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

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateRequest {
        private int goalItemCount;
        private int price;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Response {
        private Long categoryId;
        private String categoryName;
        private Long itemId;
        private String itemName;
        private int goalItemCount;
        private int price;
        private int totalPrice;
        private boolean checkReached;
        private double goalPercent;
        private int savedItemCount;
        private LocalDateTime createdAt;
        private LocalDateTime reachedAt;

        public Response(GoalItem goalItem){
            this.categoryId = goalItem.getItem().getCategory().getId();
            this.categoryName = goalItem.getItem().getCategory().getName();
            this.itemId = goalItem.getItem().getId();
            this.itemName = goalItem.getItem().getName();
            this.goalItemCount = goalItem.getCount();
            this.price = (goalItem.getCount() == 0)? 0 : goalItem.getTotal() / goalItem.getCount();
            this.totalPrice = goalItem.getTotal();
            this.checkReached = goalItem.isCheckReached();
            this.goalPercent = goalItem.getGoalPercent();
            this.savedItemCount = (goalItem.getSavedItems() == null) ? 0 : goalItem.getSavedItems().size();
            this.createdAt = goalItem.getCreatedDate();
            this.reachedAt = goalItem.getReachedAt();

        }
    }


}
