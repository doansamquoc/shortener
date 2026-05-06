package dev.sam.shortener.service.impl;

import dev.sam.shortener.dto.request.UrlCreationRequest;
import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.entity.Url;
import dev.sam.shortener.mapper.UrlMapper;
import dev.sam.shortener.repository.UrlRepository;
import dev.sam.shortener.util.Base62Encoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {
	@Mock
	private UrlRepository repository;

	@Spy
	private UrlMapper mapper = Mappers.getMapper(UrlMapper.class);

	@InjectMocks
	private UrlServiceImpl service;

	private UrlCreationRequest request;
	private Url url;

	@Test
	@DisplayName("The short code provided")
	void shouldSaveGetRedirectUrl_WhenShortCodeProvided() {
		UrlCreationRequest request = new UrlCreationRequest("https://google.com", "my-custom-link");
		Url url = mapper.toEntity(request);

		when(repository.save(any(Url.class))).thenReturn(url);

		UrlResponse response = service.create(request);

		assertNotNull(response);
		assertEquals("my-custom-link", response.shortenedUrl());
		verify(repository, times(1)).save(any(Url.class));
		verify(repository, never()).saveAndFlush(any());
	}

	@Test
	@DisplayName("The short code not provided")
	void shouldNotSaveGetRedirectUrl_WhenShortCodeNotProvided() {
		UrlCreationRequest request = new UrlCreationRequest("https://google.com", null);

		Url initialUrl = new Url(null, "https://google.com", null, null, null, null);
		Url savedUrl = Url.builder().id(123L).originalUrl("https://google.com").build();

		when(repository.saveAndFlush(any(Url.class))).thenReturn(savedUrl);

		UrlResponse response = service.create(request);

		String expectedShortCode = Base62Encoder.encode(123L);

		assertNotNull(response);
		assertEquals(expectedShortCode, response.shortenedUrl());
		verify(repository, times(1)).saveAndFlush(any(Url.class));
		verify(repository, never()).save(any());
	}
}