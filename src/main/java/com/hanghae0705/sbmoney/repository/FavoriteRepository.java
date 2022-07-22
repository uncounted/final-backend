package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.user.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser_Id(Long userId);
    List<Favorite> findByUserId(Long userId);
}
