package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.*;
import com.hanghae0705.sbmoney.model.dto.SavedItemForStatisticsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.transaction.Transaction;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticsMyMonthRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    public StatisticsMyMonthRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    // 월별, 일별 Data 생성
    // savedItem에서 entity들을 뽑아와 날짜로 월별, 일별 통계 테이블에 저장하는 용도
    // 얘는 controller의 스케쥴러에서 사용한다.
    public List<SavedItemForStatisticsDto> createStatisticByUsername(String username, LocalDateTime startDate, LocalDateTime endDate) {
        QSavedItem savedItem = QSavedItem.savedItem;
        // ERD 수정하고 싶다
        // 얜 또 뭐야
        NumberPath<Integer> aliasOrderType = Expressions.numberPath(Integer.class, "totalPrice");
        List<SavedItemForStatisticsDto> tempList = jpaQueryFactory.select(
                Projections.bean(SavedItemForStatisticsDto.class,
                    savedItem.user.username.as("username"),
                    savedItem.item.name.as("itemName"),
                    savedItem.count().as("totalCount"),
                    savedItem.price.as("totalPrice"),
                    savedItem.item.category.name.as("categoryName")))
                .from(savedItem)
                .where(savedItem.user.username.eq(username), savedItem.createdAt.between(startDate, endDate))
                .orderBy(aliasOrderType.desc()).groupBy(savedItem.item.id)
                .limit(5).fetch();
        return tempList;
    }

    public List<StatisticsMyMonth> findMyMonthlyStatisticsByUsernameAndPrice(String username, String standardDate) {
        QStatisticsMyMonth monthlyPrice = QStatisticsMyMonth.statisticsMyMonth;
        List<StatisticsMyMonth> result = jpaQueryFactory.select(Projections.fields(StatisticsMyMonth.class,
                        monthlyPrice.standardDate,
                        monthlyPrice.rankPrice,
                        monthlyPrice.itemName,
                        monthlyPrice.totalPrice,
                        monthlyPrice.username
                ))
                .from(monthlyPrice)
                .where(monthlyPrice.username.eq(username),
                        monthlyPrice.standardDate.eq(standardDate))
                .orderBy(monthlyPrice.rankPrice.asc())
                .fetch();

        return result;
    }

    // 횟수 오름차순
    public List<StatisticsMyMonth> findMyMonthlyStatisticsByUsernameAndCount(String username, String standardDate) {
        QStatisticsMyMonth monthlyCnt = QStatisticsMyMonth.statisticsMyMonth;

        List<StatisticsMyMonth> result = jpaQueryFactory.select(Projections.fields(StatisticsMyMonth.class,
                        monthlyCnt.standardDate,
                        monthlyCnt.rankCnt,
                        monthlyCnt.itemName,
                        monthlyCnt.totalCnt,
                        monthlyCnt.username
                ))
                .from(monthlyCnt)
                .where(monthlyCnt.username.eq(username),
                        monthlyCnt.standardDate.eq(standardDate))
                .orderBy(monthlyCnt.rankCnt.asc())
                .fetch();

        return result;
    }

    @Transactional
    public void saveMyMonthlyStatistics(StatisticsMyMonth statisticsMymonth) {
        entityManager.persist(statisticsMymonth);
    }
}
