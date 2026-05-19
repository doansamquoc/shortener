package dev.sam.shortener.repository;

import dev.sam.shortener.entity.RefreshToken;
import dev.sam.shortener.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);

	List<RefreshToken> findAllByUser(User user);
}
