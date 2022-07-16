//package com.hanghae0705.sbmoney.repository;
//
//import com.hanghae0705.sbmoney.model.domain.QSavedItem;
//import com.hanghae0705.sbmoney.model.domain.QStatisticsMyDay;
//import com.hanghae0705.sbmoney.model.domain.StatisticsAllUserDay;
//import com.hanghae0705.sbmoney.model.domain.StatisticsMyDay;
//import com.hanghae0705.sbmoney.model.dto.SaverdItemForStatisticsAllUserDto;
//import com.querydsl.core.types.Projections;
//import com.querydsl.core.types.dsl.Expressions;
//import com.querydsl.core.types.dsl.NumberPath;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.time.LocalDateTime;
//import java.util.List;
//
//
//
//@Repository
//@Slf4j
//public class StatisticsAllUserRepository {
//    private final EntityManager em;
//    private final JPAQueryFactory queryFactory;
//
//    public StatisticsAllUserRepository(EntityManager em) {
//        this.em = em;
//        this.queryFactory = new JPAQueryFactory(em);
//    }
//
//    // userId 기반으로 시작일, 종료일로 끊어서 데이터 받아오기
//    public List<SaverdItemForStatisticsAllUserDto> findByDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
//        QSavedItem savedItem = QSavedItem.savedItem;
//        NumberPath<Integer> aliasOrderType = Expressions.numberPath(Integer.class, "totalPrice");
//
//        List<SaverdItemForStatisticsAllUserDto> result = queryFactory
//                .select(Projections.fields(SaverdItemForStatisticsAllUserDto.class,
//                        savedItem.item.name.as("itemName"),
//                        savedItem.price.sum().as("totalPrice"),
//                        savedItem.count().as("totalCount")
//                ))
//                .from(savedItem)
//                .where(savedItem.createdAt.between(startDateTime, endDateTime))
//                .groupBy(savedItem.item.id)
//                .orderBy(aliasOrderType.desc())
//                .limit(5)
//                .fetch();
//
//        // log
//        result.forEach(SaverdItemForStatisticsAllUserDto -> {
//            log.info("itemName: "+SaverdItemForStatisticsAllUserDto.getItemName());
//            log.info("totalPrice: "+SaverdItemForStatisticsAllUserDto.getTotalPrice());
//            log.info("totalCount: "+SaverdItemForStatisticsAllUserDto.getTotalCount());
//        });
//        return result;
//    }
//
//    @Transactional
//    public void saveStatisticsAllDay(StatisticsAllUserDay statisticsAllUserDay) {
//        em.persist(statisticsAllUserDay);
//    }
//
//    public List<StatisticsAllUserDay> findAllDailyByPrice(String standardDate) {
//        QStatisticsMyDay myDayPrice = QStatisticsMyDay.statisticsMyDay;
//
//        List<StatisticsAllUserDay> result = queryFactory.select(Projections.fields(StatisticsMyDay.class,
//                        myDayPrice.standardDate,
//                        myDayPrice.rankPrice,
//                        myDayPrice.itemName,
//                        myDayPrice.totalPrice
//                ))
//                .from(myDayPrice)
//                .where(myDayPrice.standardDate.eq(standardDate))
//                .orderBy(myDayPrice.rankPrice.asc())
//                .fetch();
//
//        //log
//        result.forEach(StatisticsMyDay -> {
//            log.info(StatisticsMyDay.getStandardDate() + " | "
//                    + StatisticsMyDay.getRankPrice() + " | "
//                    + StatisticsMyDay.getItemName() + " | "
//                    + StatisticsMyDay.getTotalPrice());
//        });
//
//        return result;
//    }
//
//    public List<StatisticsMyDay> findMyDailyByUserIdAndCount(Long userId, String standardDate) {
//        QStatisticsMyDay myDayCount = QStatisticsMyDay.statisticsMyDay;
//
//        List<StatisticsMyDay> result = queryFactory.select(Projections.fields(StatisticsMyDay.class,
//                        myDayCount.standardDate,
//                        myDayCount.rankCount,
//                        myDayCount.itemName,
//                        myDayCount.totalCount
//                ))
//                .from(myDayCount)
//                .where(myDayCount.standardDate.eq(standardDate))
//                .orderBy(myDayCount.rankCount.asc())
//                .fetch();
//
//        //log
//        result.forEach(StatisticsMyDay -> {
//            log.info(StatisticsMyDay.getStandardDate() + " | "
//                    + StatisticsMyDay.getRankCount() + " | "
//                    + StatisticsMyDay.getItemName() + " | "
//                    + StatisticsMyDay.getRankCount());
//        });
//
//        return result;
//    }
//}
//
