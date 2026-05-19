package dev.sam.shortener.service.impl;

import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.response.UserDetailsResponse;
import dev.sam.shortener.mapper.UserMapper;
import dev.sam.shortener.service.AdminService;
import dev.sam.shortener.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminServiceImpl implements AdminService {
    UserService userService;
    UserMapper userMapper;
    
    @Override
    public PageResponse<UserDetailsResponse> findAllUsers(String searchTerm, Double threshold, Pageable pageable) {
        if (searchTerm.isBlank()) {
            return PageResponse.from(userService.findAllUsers(pageable).map(userMapper::toDetails));
        }
        return PageResponse.from(userService.searchUsers(searchTerm, threshold, pageable).map(userMapper::toDetails));
    }
    
    @Override
    public void deleteUser(Long userId) {
        userService.delete(userId);
    }
}
