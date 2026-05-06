package dev.sam.shortener.repository;

import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.entity.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
	boolean existsByShortCode(String shortCode);

	Optional<Url> findByShortCode(String shortCode);

	Page<Url> findAllByUserId(Long userId, Pageable pageable);

	@Query("""
		SELECT u FROM Url u
		WHERE u.user.id = :userId
		AND (function('word_similarity', :searchTerm, u.title) > :threshold
		     OR function('word_similarity', :searchTerm, u.actualUrl) > :threshold)
		ORDER BY function('greatest',
		     function('word_similarity', :searchTerm, u.title),
		     function('word_similarity', :searchTerm, u.actualUrl)
		) DESC
	""")
	Page<Url> searchWordSimilarity(
		@Param("userId") Long userId,
		@Param("searchTerm") String searchTerm,
		@Param("threshold") Double threshold,
		Pageable pageable
	);
}
