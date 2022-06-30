package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
    Board findAllById(Long id);
}
