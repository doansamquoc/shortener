package dev.sam.shortener.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.sam.shortener.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRole extends Base {
	@ManyToOne
	@JsonIgnore // Avoid infinity fetch
	@JoinColumn(name = "user_id")
	User user;

	@Builder.Default
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	Role role = Role.ROLE_USER;

	public UserRole(User user) {
		this.user = user;
		this.role = Role.ROLE_USER;
	}
}
