package com.pure.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 return 하겠다는 뜻.
public class IndexController {

	@GetMapping({"","/"})
	public String index() {
		//mustache 기본폴더 src/main/resources/
		//viewResolver 설정: templates(prefix) .mustache(suffix) (기본 설정이기 때문에 .yml에서 생략가능)
		return "index"; //경로 : src/main/resources/templates/index.mustache
	}
	
	@GetMapping("/user")
	public String user() {
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
	
	@GetMapping("/login")
	public String login() {
		return "loginForm";
	}
	
	@GetMapping("/join")
	public String join() {
		return "join";
	}
	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완료됨!";
	}
}
