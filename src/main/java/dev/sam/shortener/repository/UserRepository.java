package dev.sam.shortener.repository;

import dev.sam.shortener.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@EntityGraph(attributePaths = {"roles"})
	@Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
	User findByIdentifier(String identifier);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	@EntityGraph(attributePaths = {"roles"})
	Optional<User> findUserAndRolesByEmail(String email);

	Optional<User> findByEmail(String email);

	@EntityGraph(attributePaths = {"roles"})
	@Query("""
	SELECT u FROM User u
	WHERE function('word_similarity', u.username, :searchTerm) > :threshold
	OR function('word_similarity', u.email, :searchTerm) > :threshold
	ORDER BY function('greatest', function('word_similarity', u.username, :searchTerm),function('word_similarity', u.email, :searchTerm))
	DESC
	""")
	Page<User> findAll(String searchTerm, Double threshold, Pageable pageable);

	@EntityGraph(attributePaths = {"roles"})
	@Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
	Page<User> findAll(@NonNull Pageable pageable);
}
