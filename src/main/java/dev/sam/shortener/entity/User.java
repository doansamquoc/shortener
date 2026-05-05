package dev.sam.shortener.entity;

import dev.sam.shortener.type.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends Base {
	@Column(nullable = false, unique = true)
	String username;

	@Column(nullable = false)
	String password;

	@Column(nullable = false, unique = true)
	String email;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	Set<Role> roles = Set.of(Role.USER);

	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	List<Url> urls = new ArrayList<>();
}
