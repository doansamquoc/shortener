package dev.sam.shortener.service.impl;

import dev.sam.shortener.entity.User;
import dev.sam.shortener.service.CustomUserDetailsService;
import dev.sam.shortener.service.UserService;
import dev.sam.shortener.dto.CustomUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
	UserService userService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
		User user = userService.findByIdentifier(identifier);
		return new CustomUserDetails(user);
	}
}
