package com.hanghae0705.sbmoney.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghae0705.sbmoney.model.domain.baseEntity.BaseEntity;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class GoalItem extends BaseEntity {
    @Column(name = "GOAL_ITEM")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonBackReference(value = "user-fk")
    User user;

    @Column
    LocalDateTime reachedAt;

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


}
