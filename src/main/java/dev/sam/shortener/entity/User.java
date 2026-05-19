package dev.sam.shortener.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends Base {
	@Column(name = "username")
	String username;

	@Column(name = "password")
	String password;

	@Column(name = "email")
	String email;

	@Builder.Default
	@OneToMany(
		mappedBy = "user",
		fetch = FetchType.EAGER,
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	Set<UserRole> roles = new HashSet<>();

	@Builder.Default
	@OneToMany(
		mappedBy = "user",
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	List<Url> urls = new ArrayList<>();
}
