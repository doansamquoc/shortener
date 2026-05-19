package dev.sam.shortener.dto;

import dev.sam.shortener.entity.User;
import dev.sam.shortener.entity.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
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
	Long id;
	String username;
	String email;
	String password;
	Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.authorities = enrichAuthorities(user.getRoles());
	}

	private static Collection<? extends GrantedAuthority> enrichAuthorities(Set<UserRole> roles) {
		return roles.stream()
			.map(role -> new SimpleGrantedAuthority(AUTHORIZE_PREFIX + role.getRole().name()))
			.collect(Collectors.toSet());
	}

	public User getUser() {
		return User.builder().id(id).username(username).email(email).password(password).build();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}
}
