package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.Board;
import com.hanghae0705.sbmoney.model.domain.BoardLike;
import com.hanghae0705.sbmoney.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<BoardLike, Long> {
    List<BoardLike> findAllByBoardAndLikeIsTrue(Board board);

    BoardLike findByBoardAndUser(Board board, User user);
}
