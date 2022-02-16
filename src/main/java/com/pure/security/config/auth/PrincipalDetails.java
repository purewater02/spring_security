package com.pure.security.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pure.security.model.User;

// 시큐리티가 /login 주소 요청이 오면 로그인을 진행시킴.
// 로그인이 완료되면 Security session에 로그인 정보를 넣어줌. 이 정보는 Security ContextHolder에 들어감.
// Security는 Authentication 타입만 들어감.
// Authentication 안에 들어갈 User 정보는 UserDetails 타입 객체만 가능.
// Security Session 안에 Authentication 안에 UserDetils


public class PrincipalDetails implements UserDetails {

	private User user;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//해당 유저의 권한을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {				
				return user.getRole();
			}
			
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	

}
