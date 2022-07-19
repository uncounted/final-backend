package com.hanghae0705.sbmoney.model.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.IdentityHashMap;

@Entity
@RequiredArgsConstructor
@Getter
public class StatisticsMyMonth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int rankCnt;

    @Column(nullable = false)
    private int rankPrice;

    @Column(nullable = false)
    private Long totalCnt;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private String standardDate;

    @Column(nullable = false)
    private String username;

    @Builder
    public StatisticsMyMonth(String itemName, int rankCnt, int rankPrice, Long totalCnt, int totalPrice, String standardDate, String username) {
        this.username = username;
        this.standardDate = standardDate;
        this.itemName = itemName;
        this.totalPrice = totalPrice;
        this.totalCnt = totalCnt;
        this.rankPrice = rankPrice;
        this.rankCnt = rankCnt;
    }

    @Getter
    public static class StatisticsMonthByPrice {
        private String username;
        private String itemName;
        private int totalPrice;
        private int rankPrice;
        private String categoryName;

        @Builder
        public StatisticsMonthByPrice(String username, String itemName, int totalPrice, int rankPrice, String categoryName) {
            this.username = username;
            this.itemName = itemName;
            this.totalPrice = totalPrice;
            this.rankPrice = rankPrice;
            this.categoryName = categoryName;
        }
    }

    @Getter
    public static class StatisticsMonthByCnt {
        private String username;
        private String itemName;
        private Long totalCnt;
        private int rankCnt;
        private String categoryName;

        @Builder
        public StatisticsMonthByCnt(String username, String itemName, Long totalCnt, int rankCnt, String categoryName) {
            this.username = username;
            this.itemName = itemName;
            this.totalCnt = totalCnt;
            this.rankCnt = rankCnt;
            this.categoryName = categoryName;
        }
    }
    public void changeRankCount(int rank) {
        this.rankCnt = rank;
    }
}
