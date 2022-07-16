package com.hanghae0705.sbmoney.model.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class StatisticsAllUserDay {

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
    public StatisticsAllUserDay( String standardDate, String itemName, int totalPrice, Long totalCount, int rankPrice, int rankCount) {
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
    public static class AllDailyByPrice {

        private String itemName;
        private int totalPrice;
        private int rankPrice;

        @Builder
        public AllDailyByPrice( String itemName, int totalPrice, int rankPrice) {

            this.itemName = itemName;
            this.totalPrice = totalPrice;
            this.rankPrice = rankPrice;
        }
    }

    @Getter
    public static class AllDailyByCount {

        private String itemName;
        private Long totalCount;
        private int rankCount;

        @Builder
        public AllDailyByCount( String itemName, Long totalCount, int rankCount) {
            this.itemName = itemName;
            this.totalCount = totalCount;
            this.rankCount = rankCount;
        }
    }
}


