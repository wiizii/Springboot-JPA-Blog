package com.cos.blog.repository;

import com.cos.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// DAO
// 자동으로 bean등록이 된다.
// @Repository // 생략 가능하다
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}


// JPA Naming 쿼리 전략
// SELECT * FROM user WHERE username = ? AND password = ?;
// ?에는 각 파라미터가 넘어온다.
//User findByUsernameAndPassword(String username, String password);
