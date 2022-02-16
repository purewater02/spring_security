package com.pure.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pure.security.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity	//스프링 시큐리티 필터 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션, preAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	};
	
	@Override	
	protected void configure(HttpSecurity http) throws Exception {	
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()	//위의 세 주소 이외의 나머지는 로그인 없이 접근 가능
			.and()
			.formLogin()
			.loginPage("/loginForm")	 // "/login"주소가 올 때는 로그인 페이지 보여주기
			.loginProcessingUrl("/login") //login 주소가 호출되면 시큐리티가 대신 로그인 진행
			.defaultSuccessUrl("/") //loginForm에서 로그인을 하면 인덱스로 가지만 특정페이지에서 로그인으로 넘어온 경우에는 로그인 후 이전페이지를 다시 보여줌.
			.and()
			.oauth2Login()
			.loginPage("/loginForm")
			.userInfoEndpoint()
			.userService(principalOauth2UserService); //구글 로그인 완료 후 액세스토큰, 사용자프로필 받기 / OAuth2UserRequest 객체에 정보가 담김
	}
}
