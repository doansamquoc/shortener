package dev.sam.shortener.service.impl;

import dev.sam.shortener.dto.CustomOAuth2User;
import dev.sam.shortener.entity.User;
import dev.sam.shortener.service.CustomOAuth2UserService;
import dev.sam.shortener.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomOAuth2UserServiceImpl extends CustomOAuth2UserService {
	UserService userService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		User user = userService.processOAuth2(oAuth2User.getAttribute("email"));
		return new CustomOAuth2User(user);
	}
}
