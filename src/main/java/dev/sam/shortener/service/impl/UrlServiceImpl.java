package dev.sam.shortener.service.impl;

import dev.sam.shortener.config.AppProperties;
import dev.sam.shortener.constant.CacheNames;
import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.request.UrlCreationRequest;
import dev.sam.shortener.dto.request.UrlUpdateRequest;
import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.entity.Url;
import dev.sam.shortener.enums.ErrorCode;
import dev.sam.shortener.exception.AppException;
import dev.sam.shortener.mapper.UrlMapper;
import dev.sam.shortener.repository.UrlRepository;
import dev.sam.shortener.service.UrlService;
import dev.sam.shortener.service.UserService;
import dev.sam.shortener.util.Base62Encoder;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UrlServiceImpl implements UrlService {
	UrlMapper mapper;
	AppProperties props;
	UserService userService;
	UrlRepository repository;
	EntityManager entityManager;

	@Override
	@Transactional
	public UrlResponse create(Long userId, UrlCreationRequest request) {
		Url url = mapper.toEntity(request);
		url.setUser(userId==null?null:userService.getReference(userId));
		// If user enter the short code, just save and return
		if (request.shortCode() != null) return createShortCodeProvided(url);

		Long nextId = getNextId();
		String shortCode = Base62Encoder.encode(nextId);
		url.setShortCode(shortCode);
		return mapper.toDto(save(url));
	}

	@Override
	@Cacheable(value = CacheNames.URL_SHORT, key = "#shortCode")
	public String getRedirectUrl(String shortCode) {
		if (shortCode == null) return "/not-found";
		return findActualUrl(shortCode);
	}

	@Override
	public UrlResponse update(Long userId, Long id, UrlUpdateRequest request) {
		Url url = findByUserIdAndId(userId, id);

		if (request.shortCode() != null && !url.getShortCode().equals(request.shortCode()))
			if (existsByShortCode(request.shortCode())) throw AppException.of(ErrorCode.URL_CODE_EXISTS);

		mapper.toEntity(request, url);
		return mapper.toDto(save(url));
	}

	@Override
	public void delete(Long userId, Long id) {
		repository.deleteByUserIdAndId(userId, id);
	}

	@Override
	public void deleteAll(Long userId) {
		repository.deleteAllByUserId(userId);
	}

	@Override
	public void cleanupUrls() {
		int days = props.getCleanupUrlsAfterDays();
		Instant threshold = Instant.now().minus(days, ChronoUnit.DAYS);
		repository.cleanupUrls(threshold);
	}

	@Override
	@Async("clickExecutor")
	public void incrementTotalClicks(String shortCode) {
		repository.incrementTotalClicks(shortCode);
	}

	@Override
	public PageResponse<UrlResponse> searchUrl(Long userId, String searchTerm, Pageable pageable) {
		if (searchTerm == null || searchTerm.isBlank()) {
			return PageResponse.from(repository.findAllByUserId(userId, pageable).map(mapper::toDto));
		}

		Page<Url> page = repository.searchWordSimilarity(userId, searchTerm, 0.3, pageable);
		return PageResponse.from(page.map(mapper::toDto));
	}

	@Override
	public UrlResponse getUrl(String shortCode) {
		return mapper.toDto(findByShortCode(shortCode));
	}

	@Override
	public Url getReference(Long id) {
		return repository.getReferenceById(id);
	}

	private UrlResponse createShortCodeProvided(Url url) {
		if (!existsByShortCode(url.getShortCode())) return mapper.toDto(save(url));
		throw AppException.of(ErrorCode.URL_CODE_EXISTS);
	}

	private Url save(Url url) {
		return repository.save(url);
	}

	private Long getNextId() {
		return (Long) entityManager.createNativeQuery("SELECT nextVal('urls_id_seq')").getSingleResult();
	}

	private boolean existsByShortCode(String shortCode) {
		return repository.existsByShortCode(shortCode);
	}

	private Url findByShortCode(String shortCode) {
		return repository.findByShortCode(shortCode).orElseThrow(() -> AppException.of(ErrorCode.URL_NOT_FOUND));
	}

	private Url findById(Long id) {
		return repository.findById(id).orElseThrow(() -> AppException.of(ErrorCode.URL_NOT_FOUND));
	}

	private Url findByUserIdAndId(Long userId, Long id) {
		return repository.findByUserIdAndId(userId, id).orElseThrow(() -> AppException.of(ErrorCode.URL_NOT_FOUND));
	}

	private String findActualUrl(String shortCode) {
		return repository.findActualUrlByShortCode(shortCode).orElseThrow(() -> AppException.of(ErrorCode.URL_NOT_FOUND));
	}
}
