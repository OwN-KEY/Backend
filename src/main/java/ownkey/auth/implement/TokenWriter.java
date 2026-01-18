package ownkey.auth.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ownkey.auth.domain.RefreshToken;
import ownkey.auth.infrastructure.redis.RefreshTokenRepository;

@Component
@RequiredArgsConstructor
public class TokenWriter {

    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken token) {
        refreshTokenRepository.save(token);
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public void delete(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    public void deleteById(String tokenValue) {
        refreshTokenRepository.deleteById(tokenValue);
    }
}