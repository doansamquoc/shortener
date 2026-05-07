package dev.sam.shortener.service;

import dev.sam.shortener.dto.request.ClickRequest;
import dev.sam.shortener.entity.Click;

public interface ClickService {
	Click create(ClickRequest request);

	void create(Long urlId, ClickRequest request);
}
