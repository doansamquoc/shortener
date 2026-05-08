package dev.sam.shortener.repository;

import dev.sam.shortener.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :identifier OR u.email = :identifier")
	User findByIdentifier(String identifier);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	@EntityGraph(attributePaths = {"roles"})
	Optional<User> findUserAndRolesByEmail(String email);
}
