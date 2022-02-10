package com.pure.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pure.security.model.User;

// @Repository 없어도 IoC됨. JpaRepostory를 상속받았기 때문
public interface UserRepository extends JpaRepository<User, Integer> {

}
