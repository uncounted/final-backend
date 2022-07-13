package com.hanghae0705.sbmoney.service;

import com.hanghae0705.sbmoney.exception.ApiException;
import com.hanghae0705.sbmoney.exception.ApiRuntimeException;
import com.hanghae0705.sbmoney.model.domain.User;
import com.hanghae0705.sbmoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaticticsService {

    private final UserRepository userRepository;

    // 가결별/일별 나의 아낀 내역 저장하기
    public void createMyDailySaveByPrice(){
        // userId 받아오기
//        Long userId = userRepository.findByUsername(CommonService.getUsername()).orElseThrow(
//                () -> new ApiRuntimeException(ApiException.NOT_EXIST_USER)
//        ).getId();

        // 날짜 구하기
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        System.out.println(yesterday);

        // username으로 savedItem 일별 리스트를 높은 가격순으로 구해오기

    }


//    Table saved_item {
//        id long [pk]
//        user_id long [ref: > U.id]
//        price int
//        item_id long [ref: > item.id]
//        created_at timestamp //사용자가 변경 불가
//        goal_item_id long [ref: > goal_item.id] //check_reached 변경 시점에 업데이트
//        //saved_desc varchar
//    }

    //SELECT *
    //FROM test
    //WHERE date BETWEEN '2022-07-01 00:00:00' AND '2022-07-31 23:59:59';

   // SELECT * FROM table_a WHERE create_dt BETWEEN DATE_ADD(NOW(), INTERVAL -1 DAY ) AND NOW();

}
