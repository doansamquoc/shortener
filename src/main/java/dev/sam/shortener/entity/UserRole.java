package dev.sam.shortener.entity;

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
	@JoinColumn(name = "user_id")
	@ManyToOne
	User user;

	@Builder.Default
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	Role role = Role.ROLE_USER;
}
