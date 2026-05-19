package dev.sam.shortener.mapper;

import dev.sam.shortener.dto.request.ClickRequest;
import dev.sam.shortener.dto.response.ClickResponse;
import dev.sam.shortener.entity.Click;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClickMapper {
	@Mapping(target = "url", ignore = true)
	Click toEntity(ClickRequest request);

	ClickResponse toDto(Click click);
}
