package com.hanghae0705.sbmoney.repository;

import com.hanghae0705.sbmoney.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String userid);
    Optional<User> findById(Long id);
}
