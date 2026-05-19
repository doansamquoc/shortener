package dev.sam.shortener.repository;

import dev.sam.shortener.entity.Click;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {
    
    @Query(
        """
            SELECT c FROM Click c
            WHERE c.url.id = :urlId
            AND(function('word_similarity', :searchTerm, c.countryCode) > :threshold OR function('word_similarity', :searchTerm, c.referrer) > :threshold)
            ORDER BY function('greatest', function('word_similarity', :searchTerm, c.countryCode), function('word_similarity', :searchTerm, c.referrer)) DESC
            """
    )
    Page<Click> search(Long urlId, String searchTerm, Double threshold, Pageable pageable);
    
    Page<Click> findAllByUrlId(Long urlId, Pageable pageable);
}
