package com.hanghae0705.sbmoney.model.domain;

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
}
