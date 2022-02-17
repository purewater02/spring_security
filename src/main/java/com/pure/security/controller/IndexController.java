package com.pure.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pure.security.config.auth.PrincipalDetails;
import com.pure.security.model.User;
import com.pure.security.repository.UserRepository;

@Controller // View를 return 하겠다는 뜻.
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;  
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	//세션정보 확인을 위한 테스트 페이지
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication, // 이 방법으로는 authentication이 오브젝트 이기 때문에 캐스팅 필요
			@AuthenticationPrincipal PrincipalDetails userDetails) { // DI 방식은 다형성에 의해 바로 User를 받아올 수 있음.
		System.out.println("==========================/test/login ===========================");
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		System.out.println("authentication: "+principalDetails.getUser());
		
		System.out.println("userDetails: " + userDetails.getUser());
		return "세션 정보 확인하기";
	}
	
	//oauth로그인 세션 테스트
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication, // 이 방법으로는 authentication이 오브젝트 이기 때문에 캐스팅 필요
			@AuthenticationPrincipal PrincipalDetails userDetails) { // DI 방식은 다형성에 의해 바로 User를 받아올 수 있음.
		System.out.println("==========================/test/oauth/login ===========================");
		OAuth2User oauth2User = (OAuth2User)authentication.getPrincipal();
		System.out.println("authentication: "+oauth2User.getAttributes());	
		
		return "세션 정보 확인하기";
	}
	
	
	@GetMapping({"","/"})
	public String index() {
		//mustache 기본폴더 src/main/resources/
		//viewResolver 설정: templates(prefix) .mustache(suffix) (기본 설정이기 때문에 .yml에서 생략가능)
		return "index"; //경로 : src/main/resources/templates/index.mustache
	}
	
	//OAuth 로그인을 하든, 일반 로그인을 하든 PrincipalDetails로 받아짐.
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails: "+principalDetails.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user);
		return "redirect:/loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //함수가 실행되기 전에 권한 필터링
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "data";
	}


}
