package dev.sam.shortener.repository;

import dev.sam.shortener.entity.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
	boolean existsByShortCode(String shortCode);

	Optional<Url> findByShortCode(String shortCode);

	@Query("SELECT u.actualUrl FROM Url u WHERE u.shortCode = :shortCode")
	Optional<String> findActualUrlByShortCode(String shortCode);

	@Modifying
	@Transactional
	@Query("UPDATE Url u SET u.totalClicks = u.totalClicks + 1, u.lastClickAt = CURRENT_TIMESTAMP WHERE u.shortCode = :shortCode")
	void incrementTotalClicks(@Param("shortCode") String shortCode);

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

	@Transactional
	void deleteAllByUserId(Long userId);

	@Transactional
	void deleteByUserIdAndId(Long userId, Long id);

	@Modifying
	@Transactional
	@Query("DELETE Url u WHERE u.lastClickAt < :threshold")
	void cleanupUrls(@Param("threshold") Instant threshold);

	Optional<Url> findByUserIdAndId(Long userId, Long id);
}
