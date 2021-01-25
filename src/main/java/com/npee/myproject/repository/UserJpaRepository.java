package com.npee.myproject.repository;

import com.npee.myproject.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * userJpaRepository
 * JpaRepository를 상속받은 인터페이스 생성
 * 제네릭으로 객체 타입(Entity)과 Long을 사용한다.
 * */
public interface UserJpaRepository extends JpaRepository<User, Long> {
}
