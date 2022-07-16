package com.hanghae0705.sbmoney.model.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@RequiredArgsConstructor
public class StatisticsAllUserMonth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String standardDate;

    @NotNull
    private String itemName;

    @NotNull
    private int totalPrice;

    @NotNull
    private Long totalCount;

    private int rankPrice;
    private int rankCount;

    @Builder
    public StatisticsAllUserMonth(String standardDate, String itemName, int totalPrice, Long totalCount, int rankPrice, int rankCount) {
        this.standardDate = standardDate;
        this.itemName = itemName;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
        this.rankPrice = rankPrice;
        this.rankCount = rankCount;
    }

    public void changeRankCount(int rank){
        this.rankCount = rank;
    }

    @Getter
    public static class AllMonthlyByPrice {
        private final String itemName;
        private final int totalPrice;
        private final int rankPrice;

        @Builder
        public AllMonthlyByPrice(String itemName, int totalPrice, int rankPrice) {
            this.itemName = itemName;
            this.totalPrice = totalPrice;
            this.rankPrice = rankPrice;
        }
    }

    @Getter
    public static class AllMonthlyByCount {
        private String itemName;
        private Long totalCount;
        private int rankCount;

        @Builder
        public AllMonthlyByCount( String itemName, Long totalCount, int rankCount) {
            this.itemName = itemName;
            this.totalCount = totalCount;
            this.rankCount = rankCount;
        }
    }
}
