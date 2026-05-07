package dev.sam.shortener.mapper;

import dev.sam.shortener.dto.request.UserRegistrationRequest;
import dev.sam.shortener.dto.response.UserResponse;
import dev.sam.shortener.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
	User toEntity(UserRegistrationRequest request);
	UserResponse toDto(User user);
}
