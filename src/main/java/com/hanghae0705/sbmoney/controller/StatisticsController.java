package com.hanghae0705.sbmoney.controller;

import com.hanghae0705.sbmoney.model.domain.StatisticsMyDay;
import com.hanghae0705.sbmoney.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    // 프론트에서 요청하는 API
    @GetMapping("/api/statistics/mysave/{day}/price")
    public List<StatisticsMyDay.MyDailyByPrice> getMyDailySaveByPrice(@PathVariable String day){
        return statisticsService.getMyDailyByUserIdAndPrice(day);
    }

    @GetMapping("/api/statistics/mysave/{day}/count")
    public List<StatisticsMyDay.MyDailyByCount> getMyDailySaveByCount(@PathVariable String day){
        return statisticsService.getMyDailyByUserIdAndCount(day);
    }

    // 통계 테이블에 저장할 API
    @Scheduled(cron = "0 0 5 * * *") // 초, 분, 시, 일, 월, 요일 / 매일 5시로 설정되어 있음
    @GetMapping("/api/statistics/mysave/day/price")
    public void createMyDailySave(){
        statisticsService.createMyDailySave();
        log.info("createMyDailySave() 실행");
    }
}
