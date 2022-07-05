package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
