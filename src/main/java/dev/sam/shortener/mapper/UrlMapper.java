package dev.sam.shortener.mapper;

import dev.sam.shortener.dto.request.UrlCreationRequest;
import dev.sam.shortener.dto.request.UrlUpdateRequest;
import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.entity.Url;
import org.mapstruct.*;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UrlMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "totalClicks", ignore = true)
	@Mapping(target = "clicks", ignore = true)
	@Mapping(target = "lastClickAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Url toEntity(UrlCreationRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "totalClicks", ignore = true)
	@Mapping(target = "clicks", ignore = true)
	@Mapping(target = "lastClickAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void toEntity(UrlUpdateRequest request, @MappingTarget Url url);

	@Mapping(target = "shortenedUrl", source = "url.shortCode")
	UrlResponse toDto(Url url);
}
