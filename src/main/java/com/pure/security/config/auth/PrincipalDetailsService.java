package com.pure.security.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pure.security.model.User;
import com.pure.security.repository.UserRepository;

//시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetialsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행됨.
@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 로그인 폼에서 넘어온 input의 name이 꼭 username이어야 함. (인자가 String username이기 때문)
		// 만약이 이름을 다르게 하고 싶다면 securityConfig에서 .usernameParameter("")를 넣어서 이름을 바꿔주어야 함.
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			return  new PrincipalDetails(userEntity);
		}
		return null;
	}
	
}
