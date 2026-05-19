package dev.sam.shortener.service.impl;

import dev.sam.shortener.config.AppProperties;
import dev.sam.shortener.entity.RefreshToken;
import dev.sam.shortener.entity.User;
import dev.sam.shortener.enums.ErrorCode;
import dev.sam.shortener.exception.AppException;
import dev.sam.shortener.repository.RefreshTokenRepository;
import dev.sam.shortener.service.RefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
    AppProperties props;
    RefreshTokenRepository repository;
    
    @Override
    public RefreshToken create(User user) {
        RefreshToken refreshToken = new RefreshToken();
        Instant expiration = Instant.now().plusMillis(props.getRefreshTokenExpiration());
        
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(expiration);
        return repository.save(refreshToken);
    }
    
    @Override
    public RefreshToken findByToken(String token) {
        return repository.findByToken(token).orElseThrow(() -> AppException.of(ErrorCode.AUTH_UNAUTHORIZED));
    }
    
    @Override
    public void revoke(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        repository.save(refreshToken);
    }
    
    @Override
    public void revoke(String token) {
        RefreshToken refreshToken = findByToken(token);
        revoke(refreshToken);
    }
    
    @Override
    public void revokeAll(User user) {
        List<RefreshToken> refreshTokens = repository.findAllByUser(user);
        refreshTokens.forEach(rt -> rt.setRevoked(true));
        repository.saveAll(refreshTokens);
    }
}
