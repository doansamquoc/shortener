package dev.sam.shortener.service.impl;

import dev.sam.shortener.dto.request.ClickRequest;
import dev.sam.shortener.entity.Click;
import dev.sam.shortener.entity.Url;
import dev.sam.shortener.mapper.ClickMapper;
import dev.sam.shortener.repository.ClickRepository;
import dev.sam.shortener.service.ClickService;
import dev.sam.shortener.service.CountryCodeService;
import dev.sam.shortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClickServiceImpl implements ClickService {
	ClickMapper mapper;
	ClickRepository repository;
	CountryCodeService countryCodeService;
	private final UrlService urlService;

	@Override
	public Click create(ClickRequest request) {
		Click click = mapper.toEntity(request);
		return save(click);
	}

	@Override
	@Async("clickExecutor")
	public void create(Long urlId, ClickRequest request) {
		Click click = mapper.toEntity(request);

		Url url = urlService.getReference(urlId);
		click.setUrl(url);

		String countryCode = countryCodeService.getCountryCode(request.ipAddress());
		click.setCountryCode(countryCode);

		log.info(click.toString());
		save(click);
	}

	public String getIpAddress(HttpServletRequest request) {
		String xfHeader = request.getHeader("x-forwarded-for");
		if (xfHeader == null || xfHeader.isEmpty() || "unknown".equalsIgnoreCase(xfHeader)) return request.getRemoteAddr();
		return xfHeader.split(",")[0];
	}

	private Click save(Click click) {
		return repository.save(click);
	}
}
