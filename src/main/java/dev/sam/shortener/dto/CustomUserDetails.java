package dev.sam.shortener.dto;

import dev.sam.shortener.entity.User;
import dev.sam.shortener.entity.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.sam.shortener.constant.AppConstant.AUTHORIZE_PREFIX;

@Slf4j
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomUserDetails implements UserDetails {
	// User entity
	User user;

	// Jwt
	String jwtId;
	Instant jwtExpiresAt;

	// Authorize
	Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(User user) {
		this.user = user;
		this.authorities = extractAuthorities(user.getRoles());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	private static Collection<? extends GrantedAuthority> extractAuthorities(Set<UserRole> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(AUTHORIZE_PREFIX + role.getRole().name())
		).collect(Collectors.toSet());
	}
}
