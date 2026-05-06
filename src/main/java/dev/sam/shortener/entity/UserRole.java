package dev.sam.shortener.entity;

import dev.sam.shortener.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRole extends Base {
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	User user;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	Role role;
}
