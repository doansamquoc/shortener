package dev.sam.shortener.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.sam.shortener.entity.User;
import dev.sam.shortener.entity.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomOAuth2User implements OAuth2User, Serializable {
	Long id;
	String username;
	String email;
	String password;
	Collection<? extends GrantedAuthority> authorities;

	public CustomOAuth2User(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.authorities = enrichAuthorities(user.getRoles());
	}

	@Override
	public @Nullable <A> A getAttribute(String name) {
		return OAuth2User.super.getAttribute(name);
	}

	@Override
	@JsonIgnore
	public Map<String, Object> getAttributes() {
		return Map.of();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	private static Collection<? extends GrantedAuthority> enrichAuthorities(Set<UserRole> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole().name())).collect(Collectors.toSet());
	}

	@Override
	@JsonIgnore
	public String getName() {
		return this.username;
	}
}
