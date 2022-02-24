package com.pure.security.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.pure.security.config.auth.PrincipalDetails;
import com.pure.security.config.oauth.provider.FacebookUserInfo;
import com.pure.security.config.oauth.provider.GoogleUserInfo;
import com.pure.security.config.oauth.provider.NaverUserInfo;
import com.pure.security.config.oauth.provider.OAuth2UserInfo;
import com.pure.security.model.User;
import com.pure.security.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequset.getClientRegistration: "+ userRequest.getClientRegistration()); //registrationId로 어떤 OAuth로 로그인 했는지 알 수 있음.
		System.out.println("userRequset.getAccessToken: "+ userRequest.getAccessToken().getTokenValue());		
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		System.out.println("getAttributes: "+oauth2User.getAttributes());
		
		String provider = userRequest.getClientRegistration().getRegistrationId(); //google?		
		OAuth2UserInfo oAuth2UserInfo = null;
		if(provider.equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());		
		}else if(provider.equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		}else if(provider.equals("naver")){
			System.out.println("네이버 로그인 요청");			
			oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oauth2User.getAttributes().get("response"));
		}else {
			System.out.println("현재 구글과 페이스북만 지원됩니다.");
		}
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider+"_"+oAuth2UserInfo.getProviderId();
		String password = new BCryptPasswordEncoder().encode("1234");
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		//해당 username이 이미 있는지 확인
		User userEntity = userRepository.findByUsername(username);
		
		//구글로 처음 로그인 하면 자동으로 회원가입 시킴.
		if(userEntity == null) {
			System.out.println("최초 "+provider+" 로그인으로 회원가입을 진행했습니다.");
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}else {
			System.out.println("회원가입 이력이 있습니다. 자동 로그인을 진행했습니다.");
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes()); //이 PrincipalDetails가 Authentication 객체 안에 들어가게 됨.
	}
}
