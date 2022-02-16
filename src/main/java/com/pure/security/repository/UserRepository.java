package com.pure.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pure.security.model.User;

// @Repository 없어도 IoC됨. JpaRepostory를 상속받았기 때문
public interface UserRepository extends JpaRepository<User, Integer> {

	//findBy 규칙 
	// select * from user where username=1?
	public User findByUsername(String username); //JPA 쿼리 메서드

}
