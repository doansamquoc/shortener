package dev.sam.shortener.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken extends Base {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;

	@Column(name = "token")
	String token;

	@Column(name = "revoked")
	boolean revoked;

	@Column(name = "expires_at")
	Instant expiresAt;

	public boolean isExpired() {
		return Instant.now().isAfter(expiresAt);
	}
}
